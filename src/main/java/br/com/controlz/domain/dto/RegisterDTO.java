package br.com.controlz.domain.dto;

public class RegisterDTO {

	private String name;
	private String email;
	private String cell;
	private Double others;
	private Double salary;
	private byte[] photo;

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getCell() {
		return cell;
	}

	public Double getOthers() {
		return others;
	}

	public Double getSalary() {
		return salary;
	}

	public byte[] getPhoto() {
		return photo;
	}
}



