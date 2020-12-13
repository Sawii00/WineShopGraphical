package assegnamento3.src;

import java.io.Serializable;
import java.util.Random;

/**
 * The {@code Wine} class defines the different kinds of wines.
 * <p>
 * Wines are defined by the name, the producer, the year of production, some
 * technical notes and the grape type.
 * <p>
 * The {@code number} specifies how much bottles of wine are available.
 * <p>
 * Each wine is identified by a unique {@code id}.
 */
public class Wine implements Serializable
{

	private static final long serialVersionUID = -4609912367894803090L;

	/**
	 * Utility method that returns a WineType object given its string
	 * representation.
	 *
	 * @param type string representing a wine type
	 *
	 * @return WineType object
	 **/
	private static WineType parseWineType(String type)
	{
		type = type.toLowerCase().replace("é", "e");
		switch (type)
		{
		case "red":
		{
			return WineType.RED;
		}
		case "white":
		{
			return WineType.WHITE;
		}
		case "rose":
		{
			return WineType.ROSE;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
	}

	private int id;
	private String name;
	private String producer;
	private int year;
	private String technicalNotes;
	private String grapeType;
	private int amount;

	private WineType wineType;

	/**
	 * Class constructor.
	 * <p>
	 * 
	 * @param id             the id of the wine
	 * @param name           the name of the wine
	 * @param producer       the producer of the wine
	 * @param year           the year of production
	 * @param technicalNotes technical notes that describe the kind of wine
	 * @param grapeType      the grape from which are produced the wines
	 * @param amount         the number of bottles of wine
	 * @param wt             string representing the type of the wine
	 */
	public Wine(int id, String name, String producer, int year, String technicalNotes, String grapeType, int amount,
			String wt)
	{
		this.id = id;
		this.name = name;
		this.producer = producer;
		this.year = year;
		this.technicalNotes = technicalNotes;
		this.grapeType = grapeType;
		this.amount = amount;
		this.wineType = parseWineType(wt);
	}

	/**
	 * Class constructor.
	 * <p>
	 * 
	 * @param id             the id of the wine
	 * @param name           the name of the wine
	 * @param producer       the producer of the wine
	 * @param year           the year of production
	 * @param technicalNotes technical notes that describe the kind of wine
	 * @param grapeType      the grape from which are produced the wines
	 * @param amount         the number of bottles of wine
	 * @param wt             the type of the wine
	 */
	public Wine(int id, String name, String producer, int year, String technicalNotes, String grapeType, int amount,
			WineType wt)
	{
		this.id = id;
		this.name = name;
		this.producer = producer;
		this.year = year;
		this.technicalNotes = technicalNotes;
		this.grapeType = grapeType;
		this.amount = amount;
		this.wineType = wt;
	}

	/**
	 * Class constructor.
	 * <p>
	 * The id, if not specified, is generated random.
	 * 
	 * @param name           the name of the wine
	 * @param producer       the producer of the wine
	 * @param year           the year of production
	 * @param technicalNotes technical notes that describe the kind of wine
	 * @param grapeType      the grape from which are produced the wines
	 * @param amount         the number of bottles of wine
	 * @param wt             string representing the type of the wine
	 */
	public Wine(String name, String producer, int year, String technicalNotes, String grapeType, int amount, String wt)
	{
		Random r = new Random();
		this.id = r.nextInt();
		this.name = name;
		this.producer = producer;
		this.year = year;
		this.technicalNotes = technicalNotes;
		this.grapeType = grapeType;
		this.amount = amount;
		this.wineType = parseWineType(wt);
	}

	/**
	 * Class constructor.
	 * <p>
	 * The id, if not specified, is generated random.
	 * 
	 * @param name           the name of the wine
	 * @param producer       the producer of the wine
	 * @param year           the year of production
	 * @param technicalNotes technical notes that describe the kind of wine
	 * @param grapeType      the grape from which are produced the wines
	 * @param amount         the number of bottles of wine
	 * @param wt             the type of the wine
	 */
	public Wine(String name, String producer, int year, String technicalNotes, String grapeType, int amount,
			WineType wt)
	{
		Random r = new Random();
		this.id = r.nextInt();
		this.name = name;
		this.producer = producer;
		this.year = year;
		this.technicalNotes = technicalNotes;
		this.grapeType = grapeType;
		this.amount = amount;
		this.wineType = wt;
	}

	/**
	 * Getter for the wine's grape type.
	 * 
	 * @return grapeType grape type from which the wine derives.
	 */
	public String getGrapeType()
	{
		return grapeType;
	}

	/**
	 * Getter for the wine's id.
	 * 
	 * @return the wine's id.
	 */
	public int getID()
	{
		return id;
	}

	/**
	 * Getter for the wine's name.
	 * 
	 * @return the wine's name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Getter for the wine's number of bottles.
	 * 
	 * @return number the wine's number of bottles.
	 */
	public int getAmount()
	{
		return amount;
	}

	/**
	 * Getter for the wine's producer.
	 * 
	 * @return the wine's producer.
	 */
	public String getProducer()
	{
		return producer;
	}

	/**
	 * Getter for the wine's technical notes.
	 * 
	 * @return the wine's technical notes.
	 */
	public String getTechnicalNotes()
	{
		return technicalNotes;
	}

	/**
	 * Getter for the wine type.
	 *
	 * @return type of wine (ROSE, WHITE, RED)
	 **/
	public WineType getWineType()
	{
		return wineType;
	}

	/**
	 * Getter for the wine's year of production.
	 * 
	 * @return the wine's year.
	 */
	public int getYear()
	{
		return year;
	}

	/**
	 * Returns a serialized string that can be parsed by the server.
	 * <p>
	 * The fields are separated by "<>".
	 *
	 * @return serialized parseable version of the wine
	 **/
	public String serializedString()
	{
		return this.id + "<>" + this.name + "<>" + this.producer + "<>" + this.year + "<>" + this.technicalNotes + "<>"
				+ this.grapeType + "<>" + this.amount + "<>" + this.wineType.toString();
	}

	/**
	 * Setter for the wine's grape type.
	 * 
	 * @param grapeType grape type from which the wine derives.
	 */
	public void setGrapeType(String grapeType)
	{
		this.grapeType = grapeType;
	}

	/**
	 * Setter for the wine's id.
	 * 
	 * @param id of the wine.
	 */
	public void setID(int id)
	{
		this.id = id;
	}

	/**
	 * Setter for the wine's name.
	 * 
	 * @param name of the wine.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Setter for the wine's number of bottles.
	 * 
	 * @param amount the wine's number of bottles
	 */
	public void setAmount(int amount)
	{
		this.amount = amount;
	}

	/**
	 * Setter for the wine's producer.
	 * 
	 * @param producer of the wine.
	 */
	public void setProducer(String producer)
	{
		this.producer = producer;
	}

	/**
	 * Setter for the wine's technical notes.
	 * 
	 * @param technicalNotes of the wine.
	 */
	public void setTechnicalNotes(String technicalNotes)
	{
		this.technicalNotes = technicalNotes;
	}

	/**
	 * Setter for the wine type given a string.
	 *
	 * @param wineType string representation of the wine type
	 **/
	public void setWineType(String wineType)
	{
		this.wineType = parseWineType(wineType);
	}

	/**
	 * Setter for the wine type.
	 *
	 * @param wineType type of wine
	 **/
	public void setWineType(WineType wineType)
	{
		this.wineType = wineType;
	}

	/**
	 * Setter for the wine's year of production.
	 * 
	 * @param year of production of the wine.
	 */
	public void setYear(int year)
	{
		this.year = year;
	}

	/**
	 * Gives a textual representation of the wine.
	 * 
	 * @return id, name, produces, year, technical notes, grape type and amount of
	 *         bottles of the wine.
	 */
	@Override
	public String toString()
	{
		return "ID: " + this.id + ": " + this.name + ", " + this.producer + "(" + this.year + ") " + this.grapeType
				+ " AMOUNT: " + this.amount + "\nTechnical Notes: " + this.technicalNotes;
	}

};

/**
 * The enum {@code WineType} represents the type of wine.
 **/
enum WineType
{
	RED, WHITE, ROSE
}
