package cc.robart.iot.demoproject.persistent;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain object for a robot.
 *
 */
@Entity(name="robot")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Robot implements Serializable{
	
	private static final long serialVersionUID = -1652521451385022230L;

	@Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator",
        parameters = {
            @Parameter(
                name = "uuid_gen_strategy_class",
                value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
            )
        }
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
	
	@Column(unique=true, nullable=false)
	@NotNull
	private String name;
	
	@ManyToOne
    @JoinColumn(name ="fk_firmware_id")
    private Firmware hardwareVersion;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Firmware getHardwareVersion() {
		return hardwareVersion;
	}

	public void setHardwareVersion(Firmware hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==this)
			return true;
		if (obj instanceof Robot) {
			Robot robot = (Robot) obj;
				if(robot.name.equals(this.name) && robot.hardwareVersion.equals(this.hardwareVersion)) {
					return true;
				}else
					return false;
		}
		
		return false;
	}

	@Override
	public String toString() {
		return "Robot [name=" + name + ", hardwareVersion=" + hardwareVersion + "]";
	}

}
