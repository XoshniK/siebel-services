package xoshnik;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.siebel.data.SiebelPropertySet;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xoshnik.service.PropertySetUtils;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@NoArgsConstructor
@ContextConfiguration(classes = PropertySetUtils.class)
public class PropertySetUtilsTest {

	@Autowired
	private PropertySetUtils propertySetUtils;

	@Test
	public void compareEmptyPS() {
		SiebelPropertySet firstPS = new SiebelPropertySet();
		SiebelPropertySet secondPS = new SiebelPropertySet();
		assertTrue(propertySetUtils.isEqualPropertySets(firstPS, secondPS));
	}

	@Test
	public void compareSameHeadersPS() {
		SiebelPropertySet firstPS = new SiebelPropertySet();
		firstPS.setType("Type");
		firstPS.setValue("Value");
		SiebelPropertySet secondPS = new SiebelPropertySet();
		secondPS.setType("Type");
		secondPS.setValue("Value");
		assertTrue(propertySetUtils.isEqualPropertySets(firstPS, secondPS));
	}

	@Test
	public void compareDifferentHeadersPS() {
		SiebelPropertySet firstPS = new SiebelPropertySet();
		firstPS.setType("Type1");
		firstPS.setValue("Value1");
		SiebelPropertySet secondPS = new SiebelPropertySet();
		secondPS.setType("Type2");
		secondPS.setValue("Value2");
		assertFalse(propertySetUtils.isEqualPropertySets(firstPS, secondPS));
	}

	@Test
	public void compareSamePropertiesPS() {
		SiebelPropertySet firstPS = new SiebelPropertySet();
		firstPS.setProperty("Property1", "PropertyValue1");
		firstPS.setProperty("Property2", "PropertyValue2");
		SiebelPropertySet secondPS = new SiebelPropertySet();
		secondPS.setProperty("Property1", "PropertyValue1");
		secondPS.setProperty("Property2", "PropertyValue2");
		assertTrue(propertySetUtils.isEqualPropertySets(firstPS, secondPS));
	}

	@Test
	public void compareDifferentPropertiesPS() {
		SiebelPropertySet firstPS = new SiebelPropertySet();
		firstPS.setProperty("Property1", "PropertyValue1");
		firstPS.setProperty("Property2", "PropertyValue2");
		SiebelPropertySet secondPS = new SiebelPropertySet();
		secondPS.setProperty("Property1", "PropertyValue1");
		secondPS.setProperty("Property3", "PropertyValue2");
		assertFalse(propertySetUtils.isEqualPropertySets(firstPS, secondPS));
	}

	@Test
	public void compareDifferentValuesPropertiesPS() {
		SiebelPropertySet firstPS = new SiebelPropertySet();
		firstPS.setProperty("Property1", "PropertyValue1");
		firstPS.setProperty("Property2", "PropertyValue2");
		SiebelPropertySet secondPS = new SiebelPropertySet();
		secondPS.setProperty("Property1", "PropertyValue1");
		secondPS.setProperty("Property2", "PropertyValue3");
		assertFalse(propertySetUtils.isEqualPropertySets(firstPS, secondPS));
	}

	@Test
	public void compareDifferentAmountPropertiesPS() {
		SiebelPropertySet firstPS = new SiebelPropertySet();
		firstPS.setProperty("Property1", "PropertyValue1");
		firstPS.setProperty("Property2", "PropertyValue2");
		SiebelPropertySet secondPS = new SiebelPropertySet();
		secondPS.setProperty("Property1", "PropertyValue1");
		assertFalse(propertySetUtils.isEqualPropertySets(firstPS, secondPS));
	}

	@Test
	public void compareSameAmountChildrenPS() {
		SiebelPropertySet firstPS = new SiebelPropertySet();
		firstPS.addChild(new SiebelPropertySet());
		firstPS.addChild(new SiebelPropertySet());
		SiebelPropertySet secondPS = new SiebelPropertySet();
		secondPS.addChild(new SiebelPropertySet());
		secondPS.addChild(new SiebelPropertySet());
		assertTrue(propertySetUtils.isEqualPropertySets(firstPS, secondPS));
	}

	@Test
	public void compareDifferentAmountGrandChildrenPS() {
		SiebelPropertySet firstPS = new SiebelPropertySet();
		SiebelPropertySet firstChildPS = new SiebelPropertySet();
		firstChildPS.addChild(new SiebelPropertySet());
		firstChildPS.addChild(new SiebelPropertySet());
		firstPS.addChild(firstChildPS);
		SiebelPropertySet secondPS = new SiebelPropertySet();
		SiebelPropertySet secondChildPS = new SiebelPropertySet();
		secondChildPS.addChild(new SiebelPropertySet());
		secondPS.addChild(secondChildPS);
		assertFalse(propertySetUtils.isEqualPropertySets(firstPS, secondPS));
	}

	@Test
	public void compareDifferentTypeChildrenPS() {
		SiebelPropertySet firstPS = new SiebelPropertySet();
		SiebelPropertySet typeOneChildPS1 = new SiebelPropertySet();
		typeOneChildPS1.setType("Type1");
		SiebelPropertySet typeOneChildPS2 = new SiebelPropertySet();
		typeOneChildPS2.setType("Type1");
		firstPS.addChild(typeOneChildPS1);
		firstPS.addChild(typeOneChildPS2);

		SiebelPropertySet secondPS = new SiebelPropertySet();
		SiebelPropertySet typeOneChildPS3 = new SiebelPropertySet();
		typeOneChildPS3.setType("Type1");
		SiebelPropertySet typeTwoChildPS1 = new SiebelPropertySet();
		typeTwoChildPS1.setType("Type2");
		secondPS.addChild(typeOneChildPS3);
		secondPS.addChild(typeTwoChildPS1);

		assertFalse(propertySetUtils.isEqualPropertySets(firstPS, secondPS));
	}

	@Test
	public void compareComplexPS() {
		SiebelPropertySet firstPS = new SiebelPropertySet();
		SiebelPropertySet typeOneChildPS1 = new SiebelPropertySet();
		typeOneChildPS1.setType("Type1");
		SiebelPropertySet typeOneChildPS2 = new SiebelPropertySet();
		typeOneChildPS2.setType("Type1");
		SiebelPropertySet typeTwoChildPS1 = new SiebelPropertySet();
		typeTwoChildPS1.setType("Type2");
		firstPS.addChild(typeOneChildPS1);
		firstPS.addChild(typeOneChildPS2);
		firstPS.addChild(typeTwoChildPS1);

		SiebelPropertySet secondPS = new SiebelPropertySet();
		SiebelPropertySet typeOneChildPS3 = new SiebelPropertySet();
		typeOneChildPS3.setType("Type1");
		SiebelPropertySet typeTwoChildPS2 = new SiebelPropertySet();
		typeTwoChildPS2.setType("Type2");
		SiebelPropertySet typeTwoChildPS3 = new SiebelPropertySet();
		typeTwoChildPS3.setType("Type2");
		secondPS.addChild(typeOneChildPS3);
		secondPS.addChild(typeTwoChildPS2);
		secondPS.addChild(typeTwoChildPS3);

		assertFalse(propertySetUtils.isEqualPropertySets(firstPS, secondPS));
	}
}
