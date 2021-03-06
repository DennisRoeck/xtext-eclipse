/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.resource;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.ui.workspace.EclipseProjectConfigProvider;
import org.eclipse.xtext.workspace.ProjectConfigAdapter;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Holger Schill - Initial contribution and API
 */
public class SimpleResourceSetProvider implements IResourceSetProvider {

	@Inject
	private Provider<XtextResourceSet> resourceSetProvider;
	
	@Inject 
	private EclipseProjectConfigProvider projectConfigProvider;
	
	@Override
	public ResourceSet get(IProject project) {
		XtextResourceSet xtextResourceSet = resourceSetProvider.get();
		ProjectConfigAdapter.install(xtextResourceSet, projectConfigProvider.createProjectConfig(project));
		return xtextResourceSet;
	}

}
