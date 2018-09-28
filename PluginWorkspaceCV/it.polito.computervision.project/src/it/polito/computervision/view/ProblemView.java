package it.polito.computervision.view;



import javax.annotation.PostConstruct;
import javax.inject.Inject;


import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;


public class ProblemView {
	
	
	
	@Inject
    IWorkbench workbench;

	private TableViewer viewer;
    private Action showsolution;
    
    
   
    
    class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
        @Override
        public String getColumnText(Object obj, int index) {
            return getText(obj);
        }

        @Override
        public Image getColumnImage(Object obj, int index) {
            return getImage(obj);
        }

        @Override
        public Image getImage(Object obj) {
            return workbench.getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
        }
    }

    @PostConstruct
    public void createPartControl(Composite parent) {
        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
                | SWT.V_SCROLL);
        viewer.setContentProvider(ArrayContentProvider.getInstance());
        viewer.setLabelProvider(new LabelProvider());
        
        setCascadeProblem();
    }

    @Focus
    public void setFocus() {
        viewer.getControl().setFocus();
    }
    
    private void setCascadeProblem() {
    	
    	String [] problemstring = new String[] {"1. Did you instanciate the Cascade Classifier?", 
    			"2. Did you set properly the detetectMultiscale function?", 
    			"3. Did you load properly the classifier?",
    			"4. Did you draw properly the detection box?"};
    	
    	
    	viewer.setInput(problemstring);
    	
    	viewer.setLabelProvider(new ViewLabelProvider());

        // Create the help context id for the viewer's control
        workbench.getHelpSystem().setHelp(viewer.getControl(), "com.vogella.ide.e4view.viewer");
        makeCascadeActions();
        hookContextMenu();
        hookDoubleClickAction();
        
    	
    }
    
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            @Override
            public void menuAboutToShow(IMenuManager manager) {
            	ProblemView.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
    }

    private void fillContextMenu(IMenuManager manager) {
        manager.add(showsolution);
        // Other plug-ins can contribute there actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    private void makeCascadeActions() {
        showsolution = new Action() {
            @Override
            public void run() {
            	
            	IStructuredSelection selection = viewer.getStructuredSelection();
                Object obj = selection.getFirstElement();
                showMessage( obj.toString());
//                showMessage("Solution:\nCreate a new variable in your class and instatiate it using the constructor.\n"
//                		+ "es.\n ... \n  private CascadeClassifier faceCascade;\n ...\n  this.faceCascade = new CascadeClassifier();\n ...");
            }
        };
        showsolution.setText("Show Solution");
        showsolution.setToolTipText("Action 1 tooltip");
        showsolution.setImageDescriptor(workbench.getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

    
        
    }
    
    private void hookDoubleClickAction() {
        viewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                
				showsolution.run();
            }
        });
    }

   

    private void showMessage(String message) {
    	
    	int codeProblem = Character.getNumericValue(message.charAt(0));
    	String solution = "";
    	
    	switch(codeProblem) {
    	
    	case 1:
    		solution="Solution:\n\nCreate a new variable in your class and instatiate it using the constructor.\n"
    	    		+ "example:\n\n ... \n ... \n private CascadeClassifier faceCascade;\n ... \n ... \\n this.faceCascade = new CascadeClassifier(); \n ...";
    		break;
    	case 2:
    		
    		solution= "Solution: \n \n The parameters of the detectMultiscale funcions are:\r\n" + 
    				"\r\n" + 
    				"- image: Matrix of the type CV_8U containing an image where objects are detected.\r\n" + 
    				"- objects: Vector of rectangles where each rectangle contains the detected object.\r\n" + 
    				"- scaleFactor: Parameter specifying how much the image size is reduced at each image scale.\r\n" + 
    				"- minNeighbors: Parameter specifying how many neighbors each candidate rectangle should have to retain it.\r\n" + 
    				"- flags: Parameter with the same meaning for an old cascade as in the function cvHaarDetectObjects. It is not used for a new cascade.\r\n" + 
    				"- minSize: Minimum possible object size. Objects smaller than that are ignored.\r\n" + 
    				"- maxSize: Maximum possible object size. Objects larger than that are ignored.\r\n" + 
    				"\r\n" + 
    				"Example:\n  ... \n ... \n \n" + 
    				"this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());"
    				+ "\n \n ";
    		break;
    	case 3:
    		
    		solution= "Solution:\r\n" + 
    				"\r\n" + 
    				"In our case we have two different classifier: the Haar-like features and the Local Binary Patterns (LBP).\r\n" + 
    				"First of all we need to add a folder resource to our project and put the classifiers in it.\r\n" + 
    				"Every time you change your selection in the checkbox, you should load a different feature classifiers. \r\n" + 
    				"\r\n" + 
    				"Example:\r\n" + 
    				"\r\n" + 
    				"...\r\n" + 
    				"...\r\n" + 
    				"\r\n" + 
    				"    for (String xmlClassifier : classifierPath)\r\n" + 
    				"        {\r\n" + 
    				"                this.faceCascade.load(xmlClassifier);\r\n" + 
    				"        }\r\n" + 
    				"\r\n" + 
    				"...\r\n" + 
    				"...";
    		break;
    		
    	case 4:
    		
    		solution= "Solution:\r\n" + 
    				"\r\n" + 
    				"The detection function puts the result in a vector of rectangles, where each rectangle contains \r\n" + 
    				"the detected object.\r\n" + 
    				"We should extract them and draw in the current frame.\r\n" + 
    				"\r\n" + 
    				"Example:\r\n" + 
    				"\r\n" + 
    				"...\r\n" + 
    				"...\r\n" + 
    				"\r\n" + 
    				"Rect[] facesArray = faces.toArray();\r\n" + 
    				"for (int i = 0; i < facesArray.length; i++)\r\n" + 
    				"    Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);\n"
    				+ "...";
    		break;
    	
    	
    	}
    	
    	MessageDialog dialog = new MessageDialog(viewer.getControl().getShell(), "Solutions", null,
    		    solution, MessageDialog.INFORMATION, new String[] {"Ok"}, 0);
    	
    	dialog.open();
    	//int result = dialog.open();
    		
    	//System.out.println(result);
    	
    	  
    	IWorkbenchPage wp=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    	IViewPart myView=wp.findView("it.polito.computervision.project.partdescriptor.problemview");
    	//Hide the view :
    	wp.hideView(myView);
    	
//        MessageDialog.openInformation(viewer.getControl().getShell(), "Solutions",solution);
    }
    
    
	
	

}
