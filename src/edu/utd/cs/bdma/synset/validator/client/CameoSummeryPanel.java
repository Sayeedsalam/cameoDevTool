package edu.utd.cs.bdma.synset.validator.client;

import java.util.ArrayList;
import java.util.List;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.utd.cs.bdma.synset.validator.shared.customevents.CameoCodeSelectedEvent;

public class CameoSummeryPanel extends PopupPanel implements HasHandlers{

	private static final CameoServiceAsync cameoService = GWT.create(CameoService.class); 
	static EventBus eventBus = GWT.create(SimpleEventBus.class);
	
	private HandlerManager handlerManager;

	private String searchKey = "";
	
	private List<String> summeries;
	
	@UiField
	LoadingPanel loadingPanel;
	
	private static CameoSummeryPanelUiBinder uiBinder = GWT.create(CameoSummeryPanelUiBinder.class);

	interface CameoSummeryPanelUiBinder extends UiBinder<Widget, CameoSummeryPanel> {
	}

	public CameoSummeryPanel() {
		setWidget(uiBinder.createAndBindUi(this));
		handlerManager = new HandlerManager(this);
		//mainPanel.add(loadingPanel);
			
	}

	@UiField
	FlexTable summeryTable;
	
	@UiField
	TextBox searchTextBox;
	
	@UiField
	VerticalPanel mainPanel;
	
		
	public void getAndShow(final Widget widget){
		this.showRelativeTo(widget);
		loadingPanel.show();
		if (summeries == null ){
			cameoService.getCameoSummery(new AsyncCallback<List<String>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(List<String> result) {
					// TODO Auto-generated method stub
					summeries = result;
					constructTable(summeries, widget);
					loadingPanel.hide();
				}
			});
		} else {
			constructTable(summeries, widget);
			filterResult();
			loadingPanel.hide();
		}
	}

	private void constructTable(List<String> summeries2, Widget widget) {
		// TODO Auto-generated method stub
	    summeryTable.clear();
		int i = 0;
		summeryTable.setCellPadding(10);
		for(String summery: summeries){
			summeryTable.setWidget(i++, 0, new Label(summery));
		}
		GWT.log("Finished Processing");
		//this.showRelativeTo(widget);
	}

	@UiHandler("summeryTable")
	void summeryClicked(ClickEvent e){
		int row = summeryTable.getCellForEvent(e).getRowIndex();
		GWT.log(""+row);
		String cameoCode = ((Label)summeryTable.getWidget(row, 0)).getText().split(":")[0];
		//eventBus.fireEvent(new CameoCodeSelectedEvent(cameoCode));
		this.hide();
		fireEvent(new CameoCodeSelectedEvent(cameoCode));
	}

	@UiHandler("hideButton")
	void handleClick(ClickEvent event){
		this.hide();
	}

    @UiHandler("searchTextBox")
    void handleKeyPress(KeyUpEvent event){
    	
         filterResult();
    	
    }
    
    void filterResult(){
    	summeryTable.clear();
		int i = 0;
		summeryTable.setCellPadding(10);
		for(String summery: summeries){
			if (summery.contains(searchTextBox.getText()))
			summeryTable.setWidget(i++, 0, new Label(summery));
		}
		GWT.log("Finished Processing");
    }

    
	

}
