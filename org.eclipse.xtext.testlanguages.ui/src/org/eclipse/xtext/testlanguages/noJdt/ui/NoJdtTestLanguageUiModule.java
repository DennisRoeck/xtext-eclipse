/*
 * generated by Xtext
 */
package org.eclipse.xtext.testlanguages.noJdt.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.resource.containers.IAllContainersState;
import org.eclipse.xtext.ui.editor.model.IResourceForEditorInputFactory;
import org.eclipse.xtext.ui.editor.model.ResourceForIEditorInputFactory;
import org.eclipse.xtext.ui.shared.Access;

import com.google.inject.Provider;

/**
 * Use this class to register components to be used within the IDE.
 */
public class NoJdtTestLanguageUiModule
		extends
		org.eclipse.xtext.testlanguages.noJdt.ui.AbstractNoJdtTestLanguageUiModule {
	public NoJdtTestLanguageUiModule(AbstractUIPlugin plugin) {
		super(plugin);
	}

	@Override
	public Provider<IAllContainersState> provideIAllContainersState() {
		return Access.getWorkspaceProjectsState();
	}

	@Override
	public Class<? extends IResourceForEditorInputFactory> bindIResourceForEditorInputFactory() {
		return ResourceForIEditorInputFactory.class;
	}

}
