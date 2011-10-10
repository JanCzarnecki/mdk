/*
 *     This file is part of Metabolic Network Builder
 *
 *     Metabolic Network Builder is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.metabolomes.run;

import uk.ac.ebi.chemet.io.external.RunnableTask;
import uk.ac.ebi.metabolomes.descriptor.observation.JobParameters;

/**
 * PRIAMProfileSearch.java
 *
 *
 * @author johnmay
 * @date May 11, 2011
 */
public class PRIAMProfileSearch
    extends RunnableTask {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( PRIAMProfileSearch.class );

    public PRIAMProfileSearch(JobParameters params) {
        super( params );
    }


    @Override
    public void prerun() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public void postrun() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public TaskStatus getStatus() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public boolean isCompleted() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public Thread getRunnableThread() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public String getTaskDescription() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public String getTaskCommand() {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}
