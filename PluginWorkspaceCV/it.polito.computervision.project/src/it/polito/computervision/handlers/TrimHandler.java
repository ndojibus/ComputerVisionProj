package it.polito.computervision.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;


public class TrimHandler{
	
	private String editorContent;
	
//	private final TextAttribute tagAttribute = new TextAttribute(
//            Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
	
	@Execute
    public void execute(Shell shell) throws IOException, CoreException {
        
        
        /*RuleBasedScanner scanner = new RuleBasedScanner();
        IRule rule = new OpenCVNameRule(new Token(tagAttribute));
        scanner.setRules(new IRule[] { rule });
        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
        this.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        this.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);*/
        
        
        IWorkbenchPart workbenchPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart(); 
        IFile file = (IFile) workbenchPart.getSite().getPage().getActiveEditor().getEditorInput().getAdapter(IFile.class);
        if (file == null) throw new FileNotFoundException();
        editorContent = IOUtils.toString(file.getContents(), file.getCharset());
        
        boolean foundCascade = editorContent.contains("CascadeClassifier");
        //MessageDialog.openInformation(shell, "First", foundCascade+ "");
        
        if(foundCascade) {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("it.polito.computervision.project.partdescriptor.problemview");
            //System.out.println(foundCascade+ "");
        }
        
        boolean foundCanny = editorContent.contains("Canny");
        
        if(foundCanny) {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("it.polito.computervision.project.partdescriptor.cannyproblem");
            //System.out.println(foundCanny+ "");
        }
        
        //System.out.println(content);
    }
	

}
