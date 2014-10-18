package edu.arizona.biosemantics.common.log.log;

import org.aspectj.lang.annotation.Aspect;

/**
 * This is only an example
 * @author rodenhausen
 */
@Aspect
public class TraceInjection {

//	@Before("within(edu.arizona.biosemantics.common.log..*) && "
//			+ "!within(edu.arizona.biosemantics.common.log..*) && "
//			+ "execution(public * *(..))")
//	public void trace(JoinPoint joinPoint) {
//		Signature sig = joinPoint.getSignature();
//		log(LogLevel.TRACE, "Call: " + sig.getDeclaringTypeName() + " " + sig.getName() + " Arguments: " + 
//				ObjectStringifier.stringify(joinPoint.getArgs()));
//	}

}
