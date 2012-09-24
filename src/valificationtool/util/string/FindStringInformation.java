package valificationtool.util.string;

public class FindStringInformation {
	private int offset;
	private int length;

	public FindStringInformation(int offset, int length){
		setOffset(offset);
		setLength(length);
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
}
