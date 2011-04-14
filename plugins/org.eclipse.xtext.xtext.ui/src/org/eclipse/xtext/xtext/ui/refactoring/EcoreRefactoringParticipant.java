/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.xtext.ui.refactoring;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.xtext.AbstractMetamodelDeclaration;
import org.eclipse.xtext.ParserRule;
import org.eclipse.xtext.ReferencedMetamodel;
import org.eclipse.xtext.TypeRef;
import org.eclipse.xtext.XtextPackage;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.ui.refactoring.impl.AbstractRenameParticipantWrapper;
import org.eclipse.xtext.ui.refactoring.ui.IRenameElementContext;
import org.eclipse.xtext.util.Strings;

import com.google.inject.Inject;

/**
 * @author Jan Koehnlein - Initial contribution and API
 */
@SuppressWarnings("restriction")
public class EcoreRefactoringParticipant extends AbstractRenameParticipantWrapper {

	@Inject
	private IResourceDescriptions resourceDescriptions;

	@Override
	protected IRenameElementContext createRenameElementContext(IRenameElementContext triggeringContext) {
		if (triggeringContext.getTargetElementEClass() == XtextPackage.Literals.PARSER_RULE)
			return super.createRenameElementContext(triggeringContext);
		else
			return null;
	}

	@Override
	protected EObject getRenamedElement(EObject originalTarget) {
		if (originalTarget instanceof ParserRule) {
			TypeRef returnType = ((ParserRule) originalTarget).getType();
			if (returnType != null && returnType.getMetamodel() != null && returnType.getClassifier() != null) {
				URI ePackageFileURI = findEPackageFileURI(returnType.getMetamodel());
				if (ePackageFileURI != null) {
					EObject classifierProxy = EcoreFactory.eINSTANCE.create(returnType.getClassifier().eClass());
					URI eClassFileURI = ePackageFileURI.trimFragment().appendFragment(
							ePackageFileURI.fragment() + "/" + returnType.getClassifier().getName());
					((InternalEObject) classifierProxy).eSetProxyURI(eClassFileURI);
					return classifierProxy;
				}
			}
		}
		return null;
	}

	private URI findEPackageFileURI(AbstractMetamodelDeclaration metamodel) {
		for (IResourceDescription resourceDescription : resourceDescriptions.getAllResourceDescriptions()) {
			if (Strings.equal("ecore", resourceDescription.getURI().fileExtension())) {
				for (IEObjectDescription eObjectDescription : resourceDescription
						.getExportedObjectsByType(EcorePackage.Literals.EPACKAGE)) {
					if (Strings.equal(metamodel.getEPackage().getNsURI(), eObjectDescription.getQualifiedName()
							.getFirstSegment())) {
						return eObjectDescription.getEObjectURI();
					}
				}
			}
		}
		if (metamodel instanceof ReferencedMetamodel)
			getStatus().addError("Referenced metamodel " + metamodel.getEPackage() + " is not indexed");
		return null;
	}

}
