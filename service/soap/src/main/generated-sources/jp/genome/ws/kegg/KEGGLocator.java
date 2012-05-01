/**
 * KEGGLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package jp.genome.ws.kegg;

public class KEGGLocator extends org.apache.axis.client.Service implements jp.genome.ws.kegg.KEGG {

    public KEGGLocator() {
    }


    public KEGGLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public KEGGLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for KEGGPort
    private java.lang.String KEGGPort_address = "http://soap.genome.jp/keggapi/request_v6.2.cgi";

    public java.lang.String getKEGGPortAddress() {
        return KEGGPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String KEGGPortWSDDServiceName = "KEGGPort";

    public java.lang.String getKEGGPortWSDDServiceName() {
        return KEGGPortWSDDServiceName;
    }

    public void setKEGGPortWSDDServiceName(java.lang.String name) {
        KEGGPortWSDDServiceName = name;
    }

    public jp.genome.ws.kegg.KEGGPortType getKEGGPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(KEGGPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getKEGGPort(endpoint);
    }

    public jp.genome.ws.kegg.KEGGPortType getKEGGPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            jp.genome.ws.kegg.KEGGBindingStub _stub = new jp.genome.ws.kegg.KEGGBindingStub(portAddress, this);
            _stub.setPortName(getKEGGPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setKEGGPortEndpointAddress(java.lang.String address) {
        KEGGPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (jp.genome.ws.kegg.KEGGPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                jp.genome.ws.kegg.KEGGBindingStub _stub = new jp.genome.ws.kegg.KEGGBindingStub(new java.net.URL(KEGGPort_address), this);
                _stub.setPortName(getKEGGPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("KEGGPort".equals(inputPortName)) {
            return getKEGGPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("SOAP/KEGG", "KEGG");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("SOAP/KEGG", "KEGGPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("KEGGPort".equals(portName)) {
            setKEGGPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
