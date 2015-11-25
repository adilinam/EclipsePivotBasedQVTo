package org.eclipse.m2m.qvt.oml.pivot.mapping.references.util;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtOperationalEvaluationVisitor;
import org.eclipse.m2m.internal.qvt.oml.expressions.Constructor;
import org.eclipse.m2m.internal.qvt.oml.expressions.EntryOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.Helper;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingBody;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.Module;
import org.eclipse.m2m.internal.qvt.oml.expressions.ObjectExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationBody;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.AssignExp;
import org.eclipse.ocl.expressions.IteratorExp;
import org.eclipse.ocl.expressions.PropertyCallExp;

public interface TraditionalToPivotReferencesMappingVisitor extends QvtOperationalEvaluationVisitor {
	public Object visitHelper(Helper helper);

	public Object visitMappingOperation(MappingOperation mappingOperation);

	public Object visitEntryOperation(EntryOperation entryOperation);

	public Object visitModule(Module module);
	
	public Object visitAssignExp(AssignExp assignExp);

	public Object visitObjectExp(ObjectExp objectExp);
	
	public Object visitMappingBody(MappingBody mappingBody);
	
	public Object visitPropertyCallExp(PropertyCallExp<EClassifier, EStructuralFeature> callExp);
	
	public Object visitIteratorExp(IteratorExp<EClassifier, EParameter> callExp);
	
	public Object visitConstructor(Constructor constructor);

	Object visitOperationBody(OperationBody operationBody);
	

}
