package commons;

import java.util.ArrayList;

public class IngrMenuObject {
	private String phaseid;
	private String catid;
	private String desc;
	private String pcode;
	private String ingr;
	private ArrayList<ProductIngredientsObject> pioarray;

	public IngrMenuObject(String phaseid, String catid, String desc) {
		super();
		this.phaseid = phaseid;
		this.catid = catid;
		this.desc = desc;
	}

	public IngrMenuObject(String phaseid, String catid, String desc, String pcode) {
		super();
		this.phaseid = phaseid;
		this.catid = catid;
		this.desc = desc;
		this.pcode = pcode;
	}

	public IngrMenuObject(String phaseid, String catid, String desc, String ingr,
			ArrayList<ProductIngredientsObject> pioarray) {
		super();
		this.phaseid = phaseid;
		this.catid = catid;
		this.desc = desc;
		this.pioarray = pioarray;
		this.ingr = ingr;
	}

	public String getIngr() {
		return ingr;
	}

	public void setIngr(String ingr) {
		this.ingr = ingr;
	}

	public ArrayList<ProductIngredientsObject> getPioarray() {
		return pioarray;
	}

	public void setPioarray(ArrayList<ProductIngredientsObject> pioarray) {
		this.pioarray = pioarray;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getPhaseid() {
		return phaseid;
	}

	public void setPhaseid(String phaseid) {
		this.phaseid = phaseid;
	}

	public String getCatid() {
		return catid;
	}

	public void setCatid(String catid) {
		this.catid = catid;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
