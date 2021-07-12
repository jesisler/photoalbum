package photoalbum;

import org.junit.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

public class PhotoTest {
	@Test
	public void testGettersAndSetters() {
		PojoClass personPojo = PojoClassFactory.getPojoClass(Photo.class);
	    Validator validator = ValidatorBuilder.create()
	                            .with(new GetterMustExistRule())
	                            .with(new SetterMustExistRule())
	                            .with(new SetterTester())
	                            .with(new GetterTester())
	                            .build();
	    validator.validate(personPojo);
	}
}
