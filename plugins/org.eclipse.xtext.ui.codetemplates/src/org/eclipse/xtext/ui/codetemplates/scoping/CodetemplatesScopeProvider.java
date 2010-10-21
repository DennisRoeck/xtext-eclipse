/*
 * generated by Xtext
 */
package org.eclipse.xtext.ui.codetemplates.scoping;

import java.util.LinkedHashSet;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.Grammar;
import org.eclipse.xtext.ParserRule;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider;
import org.eclipse.xtext.ui.codetemplates.templates.Codetemplate;
import org.eclipse.xtext.ui.codetemplates.templates.Codetemplates;
import org.eclipse.xtext.xtext.UsedRulesFinder;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * This class contains custom scoping description.
 * 
 * see : http://www.eclipse.org/Xtext/documentation/latest/xtext.html#scoping
 * on how and when to use it 
 *
 */
public class CodetemplatesScopeProvider extends AbstractDeclarativeScopeProvider {

	public IScope scope_Codetemplate_context(Codetemplate template, EReference reference) {
		Codetemplates root = EcoreUtil2.getContainerOfType(template, Codetemplates.class);
		if (root.getLanguage() != null) {
			Grammar grammar = root.getLanguage();
			if (!grammar.eIsProxy()) {
				LinkedHashSet<AbstractRule> usedRules = Sets.newLinkedHashSet();
				new UsedRulesFinder(usedRules).compute(grammar);
				return Scopes.scopeFor(Iterables.filter(usedRules, ParserRule.class));
			}
		}
		return IScope.NULLSCOPE;
	}
	
}
