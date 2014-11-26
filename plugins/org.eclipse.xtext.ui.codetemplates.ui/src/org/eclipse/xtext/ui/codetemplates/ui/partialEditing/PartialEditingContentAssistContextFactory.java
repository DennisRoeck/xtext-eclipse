/*******************************************************************************
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.codetemplates.ui.partialEditing;

import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.ide.editor.partialEditing.IPartialEditingContentAssistParser;
import org.eclipse.xtext.ui.editor.contentassist.antlr2.ParserBasedContentAssistContextFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class PartialEditingContentAssistContextFactory extends ParserBasedContentAssistContextFactory implements IPartialEditingContentAssistContextFactory {

	private AbstractRule rule;
	private IPartialEditingContentAssistParser partialContentAssistParser;

	@Inject
	public void setPartialEditingParser(final IPartialEditingContentAssistParser partialContentAssistParser) {
		this.partialContentAssistParser = partialContentAssistParser;
	}
	
	@Override
	public Provider<? extends StatefulFactory> getStatefulFactoryProvider() {
		final Provider<? extends StatefulFactory> delegate = super.getStatefulFactoryProvider();
		return new Provider<StatefulFactory>() {
			public StatefulFactory get() {
				StatefulFactory result = delegate.get();
				result.getDelegate().setParser(partialContentAssistParser);
				if (rule != null) {
					partialContentAssistParser.initializeFor(rule);
				}
				return result;
			}
		};
	}

	public void initializeFor(AbstractRule rule) {
		this.rule = rule;
	}
	
}
