package company;

import java.sql.Date;

public class Dependency {
	private String name;
	private Date birthdate;
	private String sex;
	private String relationship;
	
	public Dependency(String name, Date birthdate, String sex, String relationship)
	{
		this.name = name;
		this.birthdate = birthdate;
		this.sex = sex;
		this.relationship = relationship;
	}
	
	@Override
	public String toString()
	{
		return "Name:\t\t" + name + "\nBirthdate:\t" + birthdate.toString() + "\nSex:\t\t" + sex + "\nRelationship:\t" + relationship;
	}

	public String getName() {
		return name;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public String getSex() {
		return sex;
	}

	public String getRelationship() {
		return relationship;
	}
}
