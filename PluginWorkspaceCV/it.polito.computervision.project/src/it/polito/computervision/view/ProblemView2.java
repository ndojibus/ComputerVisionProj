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


public class ProblemView2 {
	
	
	
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
        
        setCannyProblem();
    }

    @Focus
    public void setFocus() {
        viewer.getControl().setFocus();
    }
    
    private void setCannyProblem() {
    	
    	String [] problemstring = new String[] {"1. Did you convert the image in greyScale before using Canny?", 
    			"2. Did you blur the image before using Canny?", 
    			"3. Did you set the proper input values into Canny's function?",
    			"4. Did you fill the image with black for the edge detection?",
    			"5. Did you convert the image in HSV before using background removal?",
    			"6. Did you compute properly the Hue component mean value and threshold using it before using background removal?",
    			"7. Did you close (erosion) the image in the background removal?",
    			"8. Did you threshold the image in the background removal?",
    			"9. Did you mask the original image?"};
    	
    	
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
            	ProblemView2.this.fillContextMenu(manager);
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
    		solution="Solution:\n\nThe Canny edge detection has as input a greyScale image. In order to use it, you should convert" + 
    				"the orginal frame in a greyScale image.\r\n" + 
    				"Example:\r\n" + 
    				"\n" + 
    				"...\n" + 
    				"...\n" + 
    				"\n" + 
    				"Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);"
    				+ "\n" + 
    				"...\n" + 
    				"...";
    		break;
    	case 2:
    		
    		solution= "Solution:\n\nIn order to enhance the result you can blur the image with a convolutional kernel \n"
    				+ "like this:\n" + 
    				"\n" + 
    				"	Imgproc.blur(grayImage, detectedEdges, new Size(3, 3));";
    		break;
    	case 3:
    		
    		solution= "Solution:\n\nCanny function has this parameter:\r\n" + 
    				"	- image: Source image, grayscale\r\n" + 
    				"	- edges: output edge map; it has the same size and type as image(can be the same as the input)\r\n" + 
    				"	- threshold1: first threshold for the hysteresis procedure.\r\n" + 
    				"	- threshold2: second threshold for the hysteresis procedure (put it as threshold1*3)\r\n" + 
    				"	- apertureSize: aperture size for the Sobel() operator, put it 3\r\n" + 
    				"	- L2gradient: a flag, indicating the accuracy, put it false";
    		break;
    		
    	case 4:
    		
    		solution= "Solution:\n\nIn order to have a more clear edge detection, the suggestion is to put black the background of our original frame.\r\n" + 
    				"\r\n" + 
    				"Example\n" + 
    				"\n" + 
    				"... \n" + 
    				"... \n" + 
    				"	Mat dest = new Mat();\r\n" + 
    				"	Core.add(dest, Scalar.all(0), dest);\r\n" + 
    				"	frame.copyTo(dest, detectedEdges);\r\n" + 
    				"	\r\n" + 
    				"...\r\n" + 
    				"...\r\n" + 
    				"";
    		break;
    		
    	case 5:
    		
    		solution= "Solution:\n\nIn order to detect the background we should convert the original frame in HSV. Example:\r\n" + 
    				"\r\n" + 
    				"	hsvImg.create(frame.size(), CvType.CV_8U);\r\n" + 
    				"	Imgproc.cvtColor(frame, hsvImg, Imgproc.COLOR_BGR2HSV);\r\n" + 
    				"	Core.split(hsvImg, hsvPlanes);";
    		break;
    	case 6:
    		solution= "Solution:\n\nThe Hue component mean value can be computed as follow:\r\n" + 
    				"\r\n" + 
    				"	Imgproc.calcHist(hue, new MatOfInt(0), new Mat(), hist_hue, histSize, new MatOfFloat(0, 179));\r\n" + 
    				"	for (int h = 0; h < 180; h++)\r\n" + 
    				"		average += (hist_hue.get(h, 0)[0] * h);\r\n" + 
    				"	average = average / hsvImg.size().height / hsvImg.size().width;\r\n" + 
    				"	\r\n" + 
    				"	Imgproc.threshold(hsvPlanes.get(0), thresholdImg, threshValue, 179.0, Imgproc.THRESH_BINARY);";
    		break;
    	case 7:
    		solution= "Solution:\n\nYou can apply erosion and dilation as big and as many time you need. This is an example:\r\n" + 
    				"\r\n" + 
    				"	Imgproc.blur(thresholdImg, thresholdImg, new Size(5, 5));\r\n" + 
    				"	Imgproc.dilate(thresholdImg, thresholdImg, new Mat(), new Point(-1, -1), 1);\r\n" + 
    				"	Imgproc.erode(thresholdImg, thresholdImg, new Mat(), new Point(-1, -1), 3);	";
    		break;
    	case 8:
    		solution= "Solution:\n\nAfter the closing you need to do a new binary threshold, as follow:\r\n" + 
    				"\r\n" + 
    				"	Imgproc.threshold(thresholdImg, thresholdImg, threshValue, 179.0, Imgproc.THRESH_BINARY);";
    		break;
    	case 9:
    		solution= "Solution:\n\nAfter all the procedure you should mask the original image with the thresholded image, as follow:\r\n" + 
    				"\r\n" + 
    				"	Mat foreground = new Mat(frame.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));\r\n" + 
    				"	frame.copyTo(foreground, thresholdImg);";
    		break;
    	
    	
    	}
    	
    	MessageDialog dialog = new MessageDialog(viewer.getControl().getShell(), "Solutions", null,
    		    solution, MessageDialog.INFORMATION, new String[] {"Ok"}, 0);
    	
    	dialog.open();
    	//int result = dialog.open();
    		
    	//System.out.println(result);
    	
    	  
    	IWorkbenchPage wp=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    	IViewPart myView=wp.findView("it.polito.computervision.project.partdescriptor.cannyproblem");
    	//Hide the view :
    	wp.hideView(myView);
    	
//        MessageDialog.openInformation(viewer.getControl().getShell(), "Solutions",solution);
    }
    
    
	
	

}
