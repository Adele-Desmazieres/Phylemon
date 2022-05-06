package AR2.Modele;

import java.io.Serializable;

public abstract class Espece implements Serializable {

	protected String sequence;

	public abstract String getNom();

	public abstract String getDescription();

	
}
