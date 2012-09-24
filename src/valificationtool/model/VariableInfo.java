package valificationtool.model;

public class VariableInfo {
	private String type;
	private String name;
	private String value;
	private int max_size;
	private int basis_location;
	private boolean defined;
	private boolean alloced;
	public VariableInfo(String _type, String _name, String _value){
		this(_type, _name, _value, 1, 0, true, false);
	}
	public VariableInfo(String _type, String _name, String _value,
			int _max_size, int _basis_location, boolean _defined, boolean _alloced){
		type = _type;
		name = _name;
		value = _value;
		max_size = _max_size;
		basis_location = _basis_location;
		defined = _defined;
		alloced = _alloced;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}

	public boolean equals(Object obj) {
		if (obj instanceof VariableInfo) {
			VariableInfo tmp = (VariableInfo) obj;
			return tmp.type.equals(type) && tmp.name.equals(name);
		}

		return super.equals(obj);
	}

	public int getMax_size() {
		return max_size;
	}

	public void setMax_size(int max_size) {
		this.max_size = max_size;
	}

	public int getBasis_location() {
		return basis_location;
	}

	public void setBasis_location(int basic_location) {
		this.basis_location = basic_location;
	}

	public boolean isDefined() {
		return defined;
	}

	public void setDefined(boolean defined) {
		this.defined = defined;
	}

	public boolean isAlloced() {
		return alloced;
	}

	public void setAlloced(boolean alloced) {
		this.alloced = alloced;
	}
}