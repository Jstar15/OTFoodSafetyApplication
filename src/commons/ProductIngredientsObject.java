package commons;

public class ProductIngredientsObject {
	private String pcode;
	private String description;
	private String labelname;
	private String meatpercentile;
	private String ingredients;
	private String nutrition;
	private String nutritionexcel;
	private String lifespan;
	private String lifespanfz;
	private String lifespantr;
	private String lifespancr;
	private String storage;
	private String packaginginfo;

	public ProductIngredientsObject(String pcode, String description, String labelname, String meatpercentile,
			String ingredients, String nutrition) {
		this.pcode = pcode;
		this.description = description;
		this.labelname = labelname;
		this.meatpercentile = meatpercentile;
		this.ingredients = ingredients;
		this.nutrition = nutrition;
	}

	public String getPackaginginfo() {
		if (packaginginfo == null) {
			return "";
		}
		return packaginginfo;
	}

	public void setPackaginginfo(String packaginginfo) {
		this.packaginginfo = packaginginfo;
	}

	public ProductIngredientsObject() {
	}

	public String getNutritionexcel() {
		return nutritionexcel;
	}

	public String getStorage() {
		return storage;
	}

	public void setStorage(String storage) {
		this.storage = storage;
	}

	public String getLifespanfz() {
		if (lifespanfz == null) {
			return "";
		}
		return lifespanfz + " days frozen";
	}

	public void setLifespanfz(String lifespanfz) {
		this.lifespanfz = lifespanfz;
	}

	public String getLifespantr() {
		if (lifespantr == null) {
			return "";
		}
		return lifespantr + " days fresh";
	}

	public void setLifespantr(String lifespantr) {
		this.lifespantr = lifespantr;
	}

	public String getLifespancr() {
		if (lifespancr == null) {
			return "";
		}
		return lifespancr + " days cryovac";
	}

	public void setLifespancr(String lifespancr) {
		this.lifespancr = lifespancr;
	}

	public String getLifespan() {
		return lifespan;
	}

	public void setLifespan(String lifespan) {
		this.lifespan = lifespan;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLabelname() {
		return labelname;
	}

	public void setLabelname(String labelname) {
		this.labelname = labelname;
	}

	public String getMeatpercentile() {
		if (meatpercentile == null) {
			return "";
		}
		return meatpercentile;
	}

	public void setMeatpercentile(String meatpercentile) {
		this.meatpercentile = meatpercentile;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getNutrition() {
		return nutrition;
	}

	public void setNutrition(String nutrition) {
		this.nutrition = nutrition;
	}

	public void setNutritionexcel(String nutritionexcel) {
		this.nutritionexcel = nutritionexcel;
	}

}
