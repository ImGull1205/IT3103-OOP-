package disc;

public class DigitalVideoDisc {
	private static int nbDigitalVideoDiscs = 0;
	private String title;
	private String category;
	private String director;
	private int length;
	private float cost;
	private int id;

	public DigitalVideoDisc(String title) {
		super();
		this.title = title;
		this.id = ++nbDigitalVideoDiscs;
	}

	public DigitalVideoDisc(String title, String category, float cost) {
		this.title = title;
		this.category = category;
		this.cost = cost;
		this.id = ++nbDigitalVideoDiscs;
	}

	public DigitalVideoDisc(String title, String category, String director, float cost) {
		this.title = title;
		this.category = category;
		this.director = director;
		this.cost = cost;
		this.id = ++nbDigitalVideoDiscs;
	}

	public DigitalVideoDisc(String title, String category, String director, int length, float cost) {
		this.title = title;
		this.category = category;
		this.director = director;
		this.length = length;
		this.cost = cost;
		this.id = ++nbDigitalVideoDiscs;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String titleSet) {
		title = titleSet;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String categorySet) {
		category = categorySet;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String directorSet) {
		director = directorSet;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int lengthSet) {
		length = lengthSet;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float costSet) {
		cost = costSet;
	}

	public int getId() {
		return id;
	}
}
