/*
 * generated by Xtext
 */
package org.eclipse.xtext.ui.tests.editor.contentassist.ide;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.xtext.ui.tests.editor.contentassist.Bug304681TestLanguageRuntimeModule;
import org.eclipse.xtext.ui.tests.editor.contentassist.Bug304681TestLanguageStandaloneSetup;
import org.eclipse.xtext.util.Modules2;

/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
public class Bug304681TestLanguageIdeSetup extends Bug304681TestLanguageStandaloneSetup {

	@Override
	public Injector createInjector() {
		return Guice.createInjector(Modules2.mixin(new Bug304681TestLanguageRuntimeModule(), new Bug304681TestLanguageIdeModule()));
	}
}
