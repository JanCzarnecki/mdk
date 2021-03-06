/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.tool.task;

import uk.ac.ebi.mdk.domain.annotation.task.ExecutableParameter;
import uk.ac.ebi.mdk.domain.annotation.task.Parameter;
import uk.ac.ebi.mdk.domain.entity.AbstractAnnotatedEntity;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.entity.Task;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * RunnableTask.java
 *
 * @author johnmay
 */
public abstract class RunnableTask
        extends AbstractAnnotatedEntity
        implements Task, Runnable {

    private TaskStatus status = TaskStatus.QUEUED;
    public static final String BASE_TYPE = "Task";

    private long nanoStart = System.nanoTime();
    ;
    private long nanoEnd;

    private Date start = new Date();
    private Date end;
    public Set<AnnotatedEntity> entities = new HashSet<AnnotatedEntity>();

    public RunnableTask() {
        this(UUID.randomUUID());
    }

    public RunnableTask(UUID uuid) {
        super(uuid);
    }


    public RunnableTask(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }

    /**
     * Returns the current elapsed time for task. If the task is completed then
     * the completion time is return
     */
    public Date getElapesedTime() {
        long elapased = System.currentTimeMillis() - start.getTime();
        return isFinished()
               ? end
               : new Date(elapased);
    }

    /**
     * The number of elapsed nano seconds since the task started.
     *
     * @return nano seconds
     */
    @Override public long elapsed() {
        return isFinished() ? nanoEnd - nanoStart
                            : System.nanoTime() - nanoStart;
    }

    /**
     * Adds an entity to the task. All added entities will be pushed for update
     * (if required) at task termination
     *
     * @param entity
     * @return
     */
    public boolean add(AnnotatedEntity entity) {
        return entities.add(entity);
    }

    /**
     * Adds a collection  entities to the task. All added entities will be
     * pushed for update (if required) at task termination
     *
     * @param entities
     * @return
     */
    public boolean addAll(Collection<? extends AnnotatedEntity> entities) {
        boolean changed = false;
        for (AnnotatedEntity entity : entities) {
            changed = add(entity) || changed;
        }
        return changed;
    }

    public Date getStart() {
        return start;
    }

    /** Returns a set of all entities added to the task */
    public Set<AnnotatedEntity> getEntities() {
        return entities;
    }

    public abstract void prerun();

    public abstract void postrun();

    public TaskStatus getStatus() {
        return this.status;
    }

    public boolean isCompleted() {
        return this.status == TaskStatus.COMPLETED;
    }

    public boolean isRunning() {
        return this.status == TaskStatus.RUNNING;
    }

    /**
     * Similar to {@see isCompleted()} but will return true if the task finished
     * in error
     */
    public boolean isFinished() {
        return this.status == TaskStatus.COMPLETED || this.status == TaskStatus.ERROR;
    }

    public void setErrorStatus() {
        end = getElapesedTime();
        nanoEnd = System.nanoTime();
        this.status = TaskStatus.ERROR;
    }

    public void setCompletedStatus() {
        end = getElapesedTime();
        nanoEnd = System.nanoTime();
        this.status = TaskStatus.COMPLETED;
    }

    public void setRunningStatus() {
        this.status = TaskStatus.RUNNING;
    }

    /**
     * Accessor for a runnable thread object
     *
     * @return A thread of this runnable object
     */
    public Thread getRunnableThread() {
        final Runnable self = this;
        return new Thread(new Runnable() {
            @Override public void run() {
                nanoStart = System.nanoTime();
                self.run();
            }
        });
    }


    /**
     * Returns the command that will be executed on the command line. If no
     * command is provided then an empty string is returned
     */
    public String getCommand() {

        StringBuilder sb = new StringBuilder(250);

        Collection<ExecutableParameter> execs = getAnnotations(ExecutableParameter.class);
        if (execs.size() > 1 || execs.isEmpty()) {
            throw new InvalidParameterException("ExecutableParamer should be unique to class but there are " + execs
                    .size());
        }

        ExecutableParameter exec = execs.iterator().next();

        sb.append(exec.getValue()).append(" ");

        for (Parameter param : getAnnotationsExtending(Parameter.class)) {
            if (!param.equals(exec)) {
                sb.append(param.getFlag()).append(" ").append(param.getValue())
                  .append(" ");
            }
        }

        return sb.toString();

    }
}
