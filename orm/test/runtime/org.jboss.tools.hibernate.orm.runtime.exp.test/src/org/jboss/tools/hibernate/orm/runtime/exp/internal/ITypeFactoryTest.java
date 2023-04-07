package org.jboss.tools.hibernate.orm.runtime.exp.internal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.hibernate.tool.orm.jbt.type.BigDecimalType;
import org.hibernate.tool.orm.jbt.type.BigIntegerType;
import org.hibernate.tool.orm.jbt.type.BooleanType;
import org.hibernate.tool.orm.jbt.type.ByteType;
import org.hibernate.tool.orm.jbt.type.CalendarDateType;
import org.hibernate.tool.orm.jbt.type.CalendarType;
import org.hibernate.tool.orm.jbt.type.CharacterType;
import org.hibernate.tool.orm.jbt.type.ClassType;
import org.hibernate.tool.orm.jbt.type.CurrencyType;
import org.hibernate.tool.orm.jbt.type.DateType;
import org.hibernate.tool.orm.jbt.type.DoubleType;
import org.hibernate.tool.orm.jbt.type.FloatType;
import org.hibernate.tool.orm.jbt.type.IntegerType;
import org.hibernate.tool.orm.jbt.type.LocaleType;
import org.hibernate.tool.orm.jbt.type.LongType;
import org.hibernate.tool.orm.jbt.type.ShortType;
import org.hibernate.tool.orm.jbt.type.StringType;
import org.hibernate.tool.orm.jbt.type.TextType;
import org.hibernate.tool.orm.jbt.type.TimeType;
import org.hibernate.tool.orm.jbt.type.TimeZoneType;
import org.hibernate.tool.orm.jbt.type.TimestampType;
import org.hibernate.tool.orm.jbt.type.TrueFalseType;
import org.hibernate.tool.orm.jbt.type.TypeFactory;
import org.hibernate.tool.orm.jbt.wrp.WrapperFactory;
import org.jboss.tools.hibernate.orm.runtime.exp.internal.util.GenericFacadeFactory;
import org.jboss.tools.hibernate.runtime.common.IFacade;
import org.jboss.tools.hibernate.runtime.spi.IType;
import org.jboss.tools.hibernate.runtime.spi.ITypeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ITypeFactoryTest {
	
	private ITypeFactory typeFactoryFacade = null;
	
	@BeforeEach
	public void beforeEach() {
		typeFactoryFacade = (ITypeFactory)GenericFacadeFactory.createFacade(
				ITypeFactory.class, 
				WrapperFactory.createTypeFactoryWrapper());
	}
	
	@Test
	public void testConstruction() {
		assertNotNull(typeFactoryFacade);
		assertSame(TypeFactory.INSTANCE, ((IFacade)typeFactoryFacade).getTarget());
	}
	
	@Test
	public void testGetBooleanType() {
		IType typeFacade = typeFactoryFacade.getBooleanType();
		assertSame(BooleanType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetByteType() {
		IType typeFacade = typeFactoryFacade.getByteType();
		assertSame(ByteType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetBigIntegerType() {
		IType typeFacade = typeFactoryFacade.getBigIntegerType();
		assertSame(BigIntegerType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetShortType() {
		IType typeFacade = typeFactoryFacade.getShortType();
		assertSame(ShortType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetCalendarType() {
		IType typeFacade = typeFactoryFacade.getCalendarType();
		assertSame(CalendarType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetCalendarDateType() {
		IType typeFacade = typeFactoryFacade.getCalendarDateType();
		assertSame(CalendarDateType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetIntegerType() {
		IType typeFacade = typeFactoryFacade.getIntegerType();
		assertSame(IntegerType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetBigDecimalType() {
		IType typeFacade = typeFactoryFacade.getBigDecimalType();
		assertSame( BigDecimalType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetCharacterType() {
		IType typeFacade = typeFactoryFacade.getCharacterType();
		assertSame(CharacterType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetClassType() {
		IType typeFacade = typeFactoryFacade.getClassType();
		assertSame(ClassType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetCurrencyType() {
		IType typeFacade = typeFactoryFacade.getCurrencyType();
		assertSame(CurrencyType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetDateType() {
		IType typeFacade = typeFactoryFacade.getDateType();
		assertSame(DateType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetDoubleType() {
		IType typeFacade = typeFactoryFacade.getDoubleType();
		assertSame(DoubleType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetFloatType() {
		IType typeFacade = typeFactoryFacade.getFloatType();
		assertSame(FloatType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetLocaleType() {
		IType typeFacade = typeFactoryFacade.getLocaleType();
		assertSame(LocaleType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetLongType() {
		IType typeFacade = typeFactoryFacade.getLongType();
		assertSame(LongType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetStringType() {
		IType typeFacade = typeFactoryFacade.getStringType();
		assertSame(StringType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetTextType() {
		IType typeFacade = typeFactoryFacade.getTextType();
		assertSame(TextType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetTimeType() {
		IType typeFacade = typeFactoryFacade.getTimeType();
		assertSame(TimeType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetTimestampType() {
		IType typeFacade = typeFactoryFacade.getTimestampType();
		assertSame(TimestampType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetTimezoneType() {
		IType typeFacade = typeFactoryFacade.getTimezoneType();
		assertSame(TimeZoneType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
	@Test
	public void testGetTrueFalseType() {
		IType typeFacade = typeFactoryFacade.getTrueFalseType();
		assertSame(TrueFalseType.INSTANCE, ((IFacade)typeFacade).getTarget());
	}
	
}
