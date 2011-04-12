package acumen.resource.model;

public class ResourceException extends Exception {

	private static final long serialVersionUID = 3499387066668231397L;
	
	private String _namespace;
	private String _name;
	
	public String getNamespace () {
		return _namespace;
	}
	
	public String getName () {
		return _name;
	}

	public ResourceException (String message) {
		super(message);
	}
	
}
