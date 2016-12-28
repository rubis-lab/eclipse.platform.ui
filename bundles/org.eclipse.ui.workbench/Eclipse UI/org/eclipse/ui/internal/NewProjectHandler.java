package org.eclipse.ui.internal;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

/**
 * @since 3.5
 *
 */

public class NewProjectHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWizard wizard = new BasicNewProjectResourceWizard();
		IStructuredSelection selection = null;
		IWorkbench workbench = null;
		wizard.init(workbench, selection);
		Shell shell = null;
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.open();
		return null;
	}
}
