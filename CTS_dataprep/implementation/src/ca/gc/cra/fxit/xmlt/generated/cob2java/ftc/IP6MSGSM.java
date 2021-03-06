package ca.gc.cra.fxit.xmlt.generated.cob2java.ftc;
import ca.gc.ccra.rccr.cobol.*;
import java.util.*;

/** GENERATED BY COB2JAVA.
	MANUAL MAINTENANCE IS NOT RECOMMENDED

	<pre>
	Record Layout:

	  Offset  Length  Occ  Type Name
	  ======  ======  ===  ==== ==========================
		0		47		1	G	Prt18SummInfo
		0		4		1	9		TransCd
		4		13		1	X		InfodecPsn
		17		30		1	X		IfaePkgRefId
	</pre>
	@see rccr.cobol.CobolRecord
*/

@SuppressWarnings({"all"})
public class IP6MSGSM extends CobolRecord
{
	private static HashMap h = new HashMap();

	private static TreeSet fields = new TreeSet(new MVComparator());

	private static int fieldCount = 0;
	private static void put( CobolField f ) {
		h.put(f.getName(), new MapValue( fieldCount++, f ));
	}

	private static void init0() {
		put(new CobolField("Prt18SummInfo",0,47,null,true));
		put(new CFNumeric("TransCd",0,4,null,0));
		put(new CobolField("InfodecPsn",4,13,null,false));
		put(new CobolField("IfaePkgRefId",17,30,null,false));
	}

	static { 
		init0();
		fields.addAll(h.values());
	}

	public IP6MSGSM()
	{
		super(h, fields);
		rec = new byte[47];

		initialize();
	}

	public String getPrt18SummInfo() { 
		return getString("Prt18SummInfo");
	}

	public void setPrt18SummInfo( String val ) { 
		set( "Prt18SummInfo", val.trim() );
	}

	public int getTransCd() { 
		return getInt("TransCd");
	}

	public void setTransCd( int val ) { 
		set( "TransCd", val );
	}

	public String getInfodecPsn() { 
		return getString("InfodecPsn");
	}

	public void setInfodecPsn( String val ) { 
		set( "InfodecPsn", val.trim() );
	}

	public String getIfaePkgRefId() { 
		return getString("IfaePkgRefId");
	}

	public void setIfaePkgRefId( String val ) { 
		set( "IfaePkgRefId", val.trim() );
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb
		.append("\n" + this.getClass().getSimpleName()+ ":infoDecPsn=").append(this.getInfodecPsn()).append(",")
		.append("getTransCd=").append(this.getTransCd()).append(",")
		.append("getPrt18SummInfo=").append(this.getPrt18SummInfo().trim()).append(",")
		.append("getRec=").append(this.getRec().trim()).append(",")
		;
		
		return sb.toString();
	}

}