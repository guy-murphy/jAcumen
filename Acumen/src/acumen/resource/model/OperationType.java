package acumen.resource.model;

public enum OperationType {
    Create(0),
    Open(1),
	Commit(2),
	Delete(3),
    Unknown(4);

	public static OperationType getOperationType(int value) throws IllegalArgumentException {
		for (OperationType operation : OperationType.values()) {
			if (operation.getValue() == value) {
				return operation;
			}
		}
		throw new IllegalArgumentException("There is no enum matching the argument you provided.");
	}

	private final int _value;

	OperationType(int value) {
		_value = value;
	}

	public int getValue() {
		return _value;
	}
}
