/*******************************************************************************
 *
 *
 *
 ******************************************************************************/

package org.eclipse.ui.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionInfo;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISources;

/**
 * Closes the active editor.
 * <p>
 * Replacement for CloseEditorAction
 * </p>
 *
 * @since 3.3
 *
 */
public class Process_Builder extends AbstractEvaluationHandler {

	private Expression enabledWhen;

	public Process_Builder() {
		registerEnablement();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		String cmdPath = "batch.bat"; //$NON-NLS-1$
		String dir = "C:\\"; //$NON-NLS-1$
		List<String> exeArgs = new ArrayList<>();
		boolean logging = false;
		try {
			FileInputStream fis = new FileInputStream(cmdPath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			String line = ""; //$NON-NLS-1$
			// System.out.println("dir : " + dir);
			List<String> cmd = new ArrayList<>();
			cmd.clear();
			cmd.add("cmd"); //$NON-NLS-1$
			cmd.add("/c"); //$NON-NLS-1$
			cmd.add("start"); //$NON-NLS-1$

			while ((line = br.readLine()) != null) {
				if (line.length() <= 1)
					break;

				String[] args = line.split(" "); //$NON-NLS-1$
				for (String s : args) {
					if (s.equals("cd")) //$NON-NLS-1$
						dir = args[1];

					if (!args[0].equals("cd")) //$NON-NLS-1$
						cmd.add(s);
				}
				if (!args[0].equals("cd")) //$NON-NLS-1$
					cmd.add("&&"); //$NON-NLS-1$

			}
			if (cmd.size() > 2)
				cmd.remove(cmd.size() - 1);
			if (exeArgs != null) {
				for (String s : exeArgs)
					cmd.add(s);
			}
			// System.out.println(cmd);
			Process p = new ProcessBuilder(cmd).directory(new File(dir)).start();
			br.close();

			if (!logging)
				return null;

			FileOutputStream log = new FileOutputStream("log.txt"); //$NON-NLS-1$

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String result = ""; //$NON-NLS-1$
			String errorLine = ""; //$NON-NLS-1$
			String linefeed = "\r\n"; //$NON-NLS-1$

			while (result != null) {
				log.write(result.getBytes());
				log.write(linefeed.getBytes());
				result = reader.readLine();
			}

			String errormsg = "--error--"; //$NON-NLS-1$
			log.write(errormsg.getBytes());
			log.write(linefeed.getBytes());

			while (errorLine != null) {
				log.write(errorLine.getBytes());
				log.write(linefeed.getBytes());
				errorLine = errorReader.readLine();
			}

			log.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * IWorkbenchWindow window =
		 * HandlerUtil.getActiveWorkbenchWindowChecked(event); IEditorPart part
		 * = HandlerUtil.getActiveEditorChecked(event);
		 * window.getActivePage().closeEditor(part, true);
		 *
		 */
		return null;
	}

	@Override
	protected Expression getEnabledWhenExpression() {
		if (enabledWhen == null) {
			enabledWhen = new Expression() {
				@Override
				public EvaluationResult evaluate(IEvaluationContext context) throws CoreException {
					IEditorPart part = InternalHandlerUtil.getActiveEditor(context);
					if (part != null) {
						return EvaluationResult.TRUE;

					}
					return EvaluationResult.TRUE;
				}

				@Override
				public void collectExpressionInfo(ExpressionInfo info) {
					info.addVariableNameAccess(ISources.ACTIVE_EDITOR_NAME);
				}
			};
		}
		return enabledWhen;
	}
}
