package org.eclipse.m2m.qvt.oml.pivot.mapping.mapping.util;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.m2m.internal.qvt.oml.expressions.Constructor;
import org.eclipse.m2m.internal.qvt.oml.expressions.ConstructorBody;
import org.eclipse.m2m.internal.qvt.oml.expressions.EntryOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.Helper;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingBody;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingCallExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModelType;
import org.eclipse.m2m.internal.qvt.oml.expressions.Module;
import org.eclipse.m2m.internal.qvt.oml.expressions.ObjectExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationBody;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;
import org.eclipse.m2m.internal.qvt.oml.expressions.ResolveExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.ResolveInExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.AssignExp;
import org.eclipse.ocl.pivot.Element;

@SuppressWarnings("restriction")
public class TraditionalQVTo2PivotReferenceVisitor extends TraditionalOCL2PivotReferenceVisitor
{
	protected static class OCLEcoreSwitch extends AbstractEcoreSwitch
	{
		public OCLEcoreSwitch(@NonNull TraditionalToPivotMapping converter) {
			super(converter);
		}

		@Override
		public Element caseEPackage(EPackage object) {
			return converter.getPivotOfEcore(org.eclipse.ocl.pivot.Package.class, object);
		}
	}
	
	public TraditionalQVTo2PivotReferenceVisitor(TraditionalToPivotMapping converter) {
		super(converter, new OCLEcoreSwitch(converter));
	}

	public Object visitAssignExp(AssignExp astNode) {
		org.eclipse.qvto.examples.pivot.imperativeocl.AssignExp pivotElement = (org.eclipse.qvto.examples.pivot.imperativeocl.AssignExp) converter.getPivot(astNode);
		return pivotElement;
	}


	public Object visitConstructor(Constructor astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.Constructor pivotElement = (org.eclipse.qvto.examples.pivot.qvtoperational.Constructor) converter.getPivot(astNode);
		return pivotElement;
	}

	public Object visitConstructorBody(ConstructorBody astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.ConstructorBody pivotElement = (org.eclipse.qvto.examples.pivot.qvtoperational.ConstructorBody) converter.getPivot(astNode);
		return pivotElement;
	}

	public Object visitEntryOperation(EntryOperation astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.EntryOperation pivotElement = (org.eclipse.qvto.examples.pivot.qvtoperational.EntryOperation) converter.getPivot(astNode);
		return pivotElement;
	}

	public Object visitHelper(Helper astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.Helper pivotElement = (org.eclipse.qvto.examples.pivot.qvtoperational.Helper)converter.getPivot(astNode);
		pivotElement.setType(resolveEClassifier(astNode.getEType()));
		pivotElement.setIsRequired(true);
		return pivotElement;
	}
	
	public Object visitMappingBody(MappingBody astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.MappingBody pivotElement = (org.eclipse.qvto.examples.pivot.qvtoperational.MappingBody) converter.getPivot(astNode);
		return pivotElement;
	}

	@Override
	public Object visitMappingCallExp(MappingCallExp astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.MappingCallExp pivotElement = (org.eclipse.qvto.examples.pivot.qvtoperational.MappingCallExp) converter.getPivot(astNode);
		//..
		return pivotElement;
	}

	public Object visitMappingOperation(MappingOperation astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.MappingOperation pivotElement = (org.eclipse.qvto.examples.pivot.qvtoperational.MappingOperation) converter.getPivot(astNode);
		pivotElement.setType(resolveEClassifier(astNode.getEType()));
		return pivotElement;
	}

	@Override
	public Object visitModelType(ModelType astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.ModelType pivotElement = (org.eclipse.qvto.examples.pivot.qvtoperational.ModelType) converter.getPivot(astNode);
		pivotElement.getMetamodel().addAll(doProcessAll(org.eclipse.ocl.pivot.Package.class, astNode.getMetamodel()));
		return pivotElement;
	}

	public Object visitModule(Module astNode) {
		if (astNode instanceof OperationalTransformation) {
			return visitOperationalTransformation((OperationalTransformation)astNode);
		}
		org.eclipse.qvto.examples.pivot.qvtoperational.Module pivotElement = (org.eclipse.qvto.examples.pivot.qvtoperational.Module) converter.getPivot(astNode);
		return pivotElement;
	}

	public Object visitObjectExp(ObjectExp astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.ObjectExp pivotElement = (org.eclipse.qvto.examples.pivot.qvtoperational.ObjectExp) converter.getPivot(astNode);
		org.eclipse.ocl.pivot.Variable referredObject = (org.eclipse.ocl.pivot.Variable) converter.getPivot(astNode.getReferredObject());
		pivotElement.setReferredObject(referredObject);
		return pivotElement;
	}

	public Object visitOperationBody(OperationBody astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody pivotElement = (org.eclipse.qvto.examples.pivot.qvtoperational.OperationBody) converter.getPivot(astNode);
		return pivotElement;
	}

	public Object visitOperationalTransformation(OperationalTransformation astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.OperationalTransformation pivotElement = (org.eclipse.qvto.examples.pivot.qvtoperational.OperationalTransformation) converter.getPivot(astNode);
		pivotElement.getUsedModelType().addAll(doProcessAll(org.eclipse.qvto.examples.pivot.qvtoperational.ModelType.class, astNode.getUsedModelType()));
		return pivotElement;
	}

	@Override
	public Object visitResolveExp(ResolveExp astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.ResolveExp pivotElement = (org.eclipse.qvto.examples.pivot.qvtoperational.ResolveExp) converter.getPivot(astNode);
		pivotElement.setType(resolveEClassifier(astNode.getEType()));
		pivotElement.setIsRequired(true);
		return pivotElement;
	}

	@Override
	public Object visitResolveInExp(ResolveInExp astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.ResolveInExp pivotElement = (org.eclipse.qvto.examples.pivot.qvtoperational.ResolveInExp) converter.getPivot(astNode);
		pivotElement.setType(resolveEClassifier(astNode.getEType()));
		pivotElement.setIsRequired(true);
		return pivotElement;
	}

	@Override
	public Object visitVarParameter(VarParameter astNode) {
		org.eclipse.qvto.examples.pivot.qvtoperational.VarParameter pivotElement = (org.eclipse.qvto.examples.pivot.qvtoperational.VarParameter) converter.getPivot(astNode);
		pivotElement.setType(resolveEClassifier(astNode.getEType()));
		pivotElement.setIsRequired(true);
		return pivotElement;
	}
}
