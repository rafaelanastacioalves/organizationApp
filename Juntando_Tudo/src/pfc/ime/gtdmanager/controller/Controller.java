package pfc.ime.gtdmanager.controller;

import java.util.Date;
import java.util.List;

import pfc.ime.gtdmanager.DataAccessLayer.DBHelper;
import pfc.ime.gtdmanager.model.ActionBox;
import pfc.ime.gtdmanager.model.CheckLine;
import android.app.Application;

public class Controller extends Application {
	private ActionBox actBox;
	
	private DBHelper dbHelper;
	
	public void setActionBox(ActionBox actBox){
		
		this.actBox = actBox; 
	}
	public void setActionBox(int id){
		if(dbHelper == null){
			dbHelper = new DBHelper(getApplicationContext());
		}
		this.actBox = dbHelper.getActionBoxById(id);
	}
	
	
	

	
	/**
	 * Loads by copying to the current List reference from the values from Database
	 */
	public void loadActionBox(){
			this.actBox.getCheckLines().clear();
			this.actBox.getCheckLines().addAll( dbHelper.getAllToDosByTag(actBox.getId())); 
	}
	
//	private List<CheckLine> getAllChecklines(DBHelper dbHlpCurrent){
//		
//		return    dbHlpCurrent.getAllToDosByTag(actBox.getId());
//	
//}
	public List<CheckLine> getAllCheckLines(){
		if ((actBox.getCheckLines()) == null){
			actBox.setCheckLines(dbHelper.getAllToDosByTag(actBox.getId()));
		}
		return actBox.getCheckLines(); 
	}
	
	public void setCheckline(List<CheckLine> lstChk){
		actBox.setCheckLines(lstChk);
	}
	

	private void addCheckLine(String strText, DBHelper dbHlpCurrent){
		CheckLine chkLnNew = new CheckLine();
		
		//----------------------setting values---------------------/
		
		// text
		chkLnNew.setText(strText); 
		
		// order
		if(actBox.getCheckLines() == null) {
			actBox.setCheckLines(getAllCheckLines());
		}
		chkLnNew.setOrder(actBox.size() + 1);
		
		//ActionBox
		chkLnNew.setActionbox_id(actBox.getId());
		
		dbHlpCurrent.createCheckLine(chkLnNew);	

		
	}
private void addCheckLine(String strText, DBHelper dbHlpCurrent, String strDateCreated){
		
		CheckLine chkLnNew = new CheckLine();
		
		//----------------------setting values---------------------//
		
		// text
		chkLnNew.setText(strText); 
		
		// order
		if(actBox.getCheckLines() == null) {
			actBox.setCheckLines(getAllCheckLines());
		}
		chkLnNew.setOrder(actBox.size() + 1);
		
		//ActionBox
		chkLnNew.setActionbox_id(actBox.getId());
		
	
		//Created at..
		
		chkLnNew.setCreated_at(strDateCreated);
		
		// ------------inserting---------------------------------//
		dbHlpCurrent.createCheckLine(chkLnNew);	


	}

/**
 *TODO: comentar funcionamento depois! E sua utiliza��o
 */
public void persist(){
	
	if(dbHelper == null){
		dbHelper = new DBHelper(getApplicationContext());
	}
	
	// ----                             atualiza informacao sobre ActionBox                       ----//
	dbHelper.updateActionBox(actBox); 
	
	// ----                  atualiza informacao sobre as Checkline contidas no ActionBox         ----//
	for (int i = 0; i < actBox.size(); i++) {
		dbHelper.updateCheckLine(actBox.getCheckLines().get(i));
	}
	
}
public void codigoTeste(){
	dbHelper = new DBHelper(this);
//	    ActionBox listaTeste = new ActionBox("bla");
	    
	// getAllActionBoxes
	//    List<ActionBox> lAB = new 	ArrayList<ActionBox>();
	//    lAB = dbHelper.getAllActionBoxes();
	//    Iterator<ActionBox> iAB = lAB.iterator();
	//    String strActionBox = "";
	//    while (iAB.hasNext()) {
	//		ActionBox actionBox = (ActionBox) iAB.next();
	//		strActionBox +=  "" + actionBox.getName() + "ID: "+ actionBox.getId() +  " \n";
	//		}
	//    loadList(1, dbHelper);
	loadActionBox();
//	lstCheckLine = getAllChecklines(dbHelper);
//	actBox.loadAllCheckLines(dbHelper);

}

public void setupBD(){
	if(dbHelper == null){
		dbHelper = new DBHelper(getApplicationContext());
	}
	dbHelper.populateActionBoxesTable();
}
public void populaActionBoxTeste(){
	for (int j = 0; j < 20; j++) {
		addCheckLine("itemTeste" + j, dbHelper, (new Date()).toString());
		loadActionBox();

	}
}

public long getId(){
	return actBox.getId();
}

public void addCheckline(String str){
	addCheckLine(str, dbHelper);
}

/**
 * Makes the Controller share the actionBox's List<Checkline> 
 * @param lstChk
 */
public void shareListWith(List<CheckLine> lstChk){
	
	// --- verifica se a lista de Checklines foi carregada ---//
	if (actBox.getCheckLines() == null){
		actBox.setCheckLines(dbHelper.getAllToDosByTag(actBox.getId()));
	}
	
	// --- agora faz o ActBox ter suas listas referenciando a lista ser compartilhada 
	
		lstChk.addAll(actBox.getCheckLines());
		actBox.setCheckLines(lstChk);
	
}
/**
 * Removes the Checkline at the referenced position holded by the current ActionBox and holded by the dataBase. No need for loadActionBox()...
 * @param position
 */
public void deleteChecklineAt(int position){
	dbHelper.deleteCheckLine(actBox.get(position).getId());
	actBox.getCheckLines().remove(position);
}

	
	
	
	
	
}