/*******************************************************************************
 * Copyright (c) 2014, 2015 Willink Transformations and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     E.D.Willink - initial API and implementation
 *******************************************************************************/
package org.eclipse.qvtd.qvto.pivot.qvtimperative.evaluation;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ocl.pivot.Element;
import org.eclipse.ocl.pivot.internal.evaluation.BasicEvaluationEnvironment;
import org.eclipse.qvtd.pivot.qvtcorebase.analysis.DomainUsage;
import org.eclipse.qvtd.pivot.qvtimperative.evaluation.QVTiExecutor;
import org.eclipse.qvtd.pivot.qvtimperative.utilities.QVTimperativeDomainUsageAnalysis;
import org.eclipse.qvto.examples.pivot.qvtoperational.OperationalTransformation;

public class QVTiRootEvaluationEnvironment extends BasicEvaluationEnvironment implements org.eclipse.qvtd.qvto.pivot.qvtimperative.evaluation.QVTiEvaluationEnvironment
{
//	private @Nullable QVTimperativeDomainUsageAnalysis usageAnalysis;

	public QVTiRootEvaluationEnvironment(@NonNull QVTiExecutor executor, @NonNull OperationalTransformation executableObject) {
		super(executor, executableObject);
	}

	@Override
	public @NonNull QVTiExecutor getExecutor() {
		return (QVTiExecutor) super.getExecutor();
	}

	@Override
	public  QVTiRootEvaluationEnvironment getRootEvaluationEnvironment() {
		return this;
	}

	@Override
	public @NonNull OperationalTransformation getTransformation() {
		Object executableObject2 = executableObject;
		assert executableObject2 != null;
		return (OperationalTransformation) executableObject2;
	}
	
	@Override
	public @Nullable DomainUsage getUsageFor(@NonNull Element element) {
		QVTimperativeDomainUsageAnalysis usageAnalysis = getExecutor().getModelManager().getTransformationAnalysis().getDomainUsageAnalysis();;
		return usageAnalysis.getUsage(element);
	}

/*	@Override
	public @Nullable DomainUsage getUsageFor(@NonNull Element element) {
		QVTimperativeDomainUsageAnalysis usageAnalysis2 = usageAnalysis;
		if (usageAnalysis2 == null) {
			usageAnalysis2 = getUsageAnalysis();
		}
		return usageAnalysis2.getUsage(element);
	}

	public @NonNull QVTimperativeDomainUsageAnalysis getUsageAnalysis() {
		QVTimperativeDomainUsageAnalysis usageAnalysis2 = usageAnalysis;
		if (usageAnalysis2 == null) {
			usageAnalysis = usageAnalysis2 = new QVTimperativeDomainUsageAnalysis(getExecutor().getEnvironmentFactory());
			usageAnalysis2.analyzeTransformation(getTransformation());
		}
		return usageAnalysis2;
	} */
}
