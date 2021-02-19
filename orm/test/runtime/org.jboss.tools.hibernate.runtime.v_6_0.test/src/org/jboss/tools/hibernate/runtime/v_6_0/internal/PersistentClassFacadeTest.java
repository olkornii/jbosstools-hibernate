package org.jboss.tools.hibernate.runtime.v_6_0.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.hibernate.mapping.Join;
import org.hibernate.mapping.JoinedSubclass;
import org.hibernate.mapping.KeyValue;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.RootClass;
import org.hibernate.mapping.SingleTableSubclass;
import org.hibernate.mapping.Subclass;
import org.hibernate.mapping.Table;
import org.hibernate.mapping.Value;
import org.jboss.tools.hibernate.runtime.common.AbstractPersistentClassFacade;
import org.jboss.tools.hibernate.runtime.common.IFacade;
import org.jboss.tools.hibernate.runtime.common.IFacadeFactory;
import org.jboss.tools.hibernate.runtime.spi.IJoin;
import org.jboss.tools.hibernate.runtime.spi.IPersistentClass;
import org.jboss.tools.hibernate.runtime.spi.IProperty;
import org.jboss.tools.hibernate.runtime.spi.ITable;
import org.jboss.tools.hibernate.runtime.spi.IValue;
import org.junit.Before;
import org.junit.Test;

public class PersistentClassFacadeTest {

	private static final IFacadeFactory FACADE_FACTORY = new FacadeFactoryImpl();
	
	private IPersistentClass persistentClassFacade = null; 
	private PersistentClass persistentClassTarget = null;
	
	@Before
	public void before() {
		persistentClassTarget = new RootClass(null);
		persistentClassFacade = new PersistentClassFacadeImpl(FACADE_FACTORY, persistentClassTarget);
	}
	
	@Test
	public void testConstruction() {
		assertNotNull(persistentClassFacade);
		assertTrue(persistentClassFacade instanceof PersistentClassFacadeImpl);
		assertSame(persistentClassTarget, ((IFacade)persistentClassFacade).getTarget());
	}
	
	@Test
	public void testGetClassName() {
		assertNotEquals("Foo", persistentClassFacade.getClassName());
		persistentClassTarget.setClassName("Foo");
		assertEquals("Foo", persistentClassFacade.getClassName());
	}
	
	@Test
	public void testGetEntityName() {
		assertNotEquals("Foo", persistentClassFacade.getEntityName());
		persistentClassTarget.setEntityName("Foo");
		assertEquals("Foo", persistentClassFacade.getEntityName());
	}
	
	@Test
	public void testIsAssignableToRootClass() {
		persistentClassTarget = new SingleTableSubclass(new RootClass(null), null);
		persistentClassFacade = new PersistentClassFacadeImpl(FACADE_FACTORY, persistentClassTarget);
		assertFalse(persistentClassFacade.isAssignableToRootClass());
		persistentClassTarget = new RootClass(null);
		persistentClassFacade = new PersistentClassFacadeImpl(FACADE_FACTORY, persistentClassTarget);
		assertTrue(persistentClassFacade.isAssignableToRootClass());
	}
	
	@Test
	public void testIsRootClass() {
		persistentClassTarget = new SingleTableSubclass(new RootClass(null), null);
		persistentClassFacade = new PersistentClassFacadeImpl(FACADE_FACTORY, persistentClassTarget);
		assertFalse(persistentClassFacade.isRootClass());
		persistentClassTarget = new RootClass(null);
		persistentClassFacade = new PersistentClassFacadeImpl(FACADE_FACTORY, persistentClassTarget);
		assertTrue(persistentClassFacade.isRootClass());
	}
	
	@Test
	public void testGetIdentifierProperty() {
		Property propertyTarget = new Property();
		assertNull(persistentClassFacade.getIdentifierProperty());
		((RootClass)persistentClassTarget).setIdentifierProperty(propertyTarget);
		IProperty propertyFacade = persistentClassFacade.getIdentifierProperty();
		assertNotNull(propertyFacade);
		assertSame(propertyTarget, ((IFacade)propertyFacade).getTarget());
	}
	
	@Test
	public void testHasIdentifierProperty() {
		assertFalse(persistentClassFacade.hasIdentifierProperty());
		((RootClass)persistentClassTarget).setIdentifierProperty(new Property());
		assertTrue(persistentClassFacade.hasIdentifierProperty());
	}
	
	@Test
	public void testIsInstanceOfRootClass() {
		assertTrue(persistentClassFacade.isInstanceOfRootClass());
		PersistentClass subClassTarget = new Subclass(persistentClassTarget, null);
		IPersistentClass subClassFacade = new PersistentClassFacadeImpl(FACADE_FACTORY, subClassTarget);
		assertFalse(subClassFacade.isInstanceOfRootClass());
	}
	
	@Test
	public void testIsInstanceOfSubclass() {
		assertFalse(persistentClassFacade.isInstanceOfSubclass());
		PersistentClass subClassTarget = new Subclass(persistentClassTarget, null);
		IPersistentClass subClassFacade = new PersistentClassFacadeImpl(FACADE_FACTORY, subClassTarget);
		assertTrue(subClassFacade.isInstanceOfSubclass());
	}
	
	@Test
	public void testGetRootClass() throws Exception {
		Field field = AbstractPersistentClassFacade.class.getDeclaredField("rootClass");
		field.setAccessible(true);
		assertNull(field.get(persistentClassFacade));
		IPersistentClass rootFacade = persistentClassFacade.getRootClass();
		assertNotNull(rootFacade);
		assertSame(rootFacade, field.get(persistentClassFacade));
		assertSame(((IFacade)rootFacade).getTarget(), persistentClassTarget);
	}
	
	@Test
	public void testGetPropertyClosureIterator() throws Exception {
		Property propertyTarget = new Property();
		PersistentClass persistentClass = new RootClass(null) {
			private static final long serialVersionUID = 1L;
			@Override
			public Iterator<?> getPropertyClosureIterator() {
				HashSet<Property> set = new HashSet<Property>();
				set.add(propertyTarget);
				return set.iterator();
			}
		};
		persistentClassFacade = new PersistentClassFacadeImpl(FACADE_FACTORY, persistentClass);
		Field field = AbstractPersistentClassFacade.class.getDeclaredField("propertyClosures");
		field.setAccessible(true);
		assertNull(field.get(persistentClassFacade));
		Iterator<IProperty> iterator = persistentClassFacade.getPropertyClosureIterator();
		assertNotNull(field.get(persistentClassFacade));
		assertTrue(iterator.hasNext());
		assertSame(propertyTarget, ((IFacade)iterator.next()).getTarget());
	}
	
	@Test
	public void testGetSuperclass() throws Exception {
		Field field = AbstractPersistentClassFacade.class.getDeclaredField("superClass");
		field.setAccessible(true);
		assertNull(field.get(persistentClassFacade));
		IPersistentClass superclassFacade = persistentClassFacade.getSuperclass();
		assertNull(field.get(persistentClassFacade));
		assertNull(superclassFacade);
		Subclass subclassTarget = new Subclass(persistentClassTarget, null);
		IPersistentClass subclassFacade = new PersistentClassFacadeImpl(FACADE_FACTORY, subclassTarget);
		assertNull(field.get(subclassFacade));
		superclassFacade = subclassFacade.getSuperclass();
		assertNotNull(superclassFacade);
		assertSame(superclassFacade, field.get(subclassFacade));
		assertSame(persistentClassTarget, ((IFacade)superclassFacade).getTarget());
	}
	
	@Test
	public void testGetPropertyIterator() throws Exception {
		Property propertyTarget = new Property();
		propertyTarget.setName("foo");
		persistentClassTarget.addProperty(propertyTarget);
		Field field = AbstractPersistentClassFacade.class.getDeclaredField("properties");
		field.setAccessible(true);
		assertNull(field.get(persistentClassFacade));
		Iterator<IProperty> propertyIterator = persistentClassFacade.getPropertyIterator();
		assertNotNull(field.get(persistentClassFacade));
		assertTrue(propertyIterator.hasNext());
		assertSame(propertyTarget, ((IFacade)propertyIterator.next()).getTarget());
	}
	
	@Test
	public void testGetProperty() throws Exception {
		Property propertyTarget = new Property();
		propertyTarget.setName("foo");
		persistentClassTarget.addProperty(propertyTarget);
		Field field = AbstractPersistentClassFacade.class.getDeclaredField("properties");
		field.setAccessible(true);
		assertNull(field.get(persistentClassFacade));
		assertNull(persistentClassFacade.getProperty("bar"));
		assertNotNull(field.get(persistentClassFacade));
		assertSame(propertyTarget, ((IFacade)persistentClassFacade.getProperty("foo")).getTarget());
		try {
			persistentClassFacade.getProperty();
			fail();
		} catch (RuntimeException e) {
			assertEquals("getProperty() is only allowed on SpecialRootClass", e.getMessage());
		}
	}
	
	@Test
	public void testGetTable() throws Exception {
		Table table = new Table();
		((RootClass)persistentClassTarget).setTable(table);
		Field field = AbstractPersistentClassFacade.class.getDeclaredField("table");
		field.setAccessible(true);
		assertNull(field.get(persistentClassFacade));
		assertSame(table, ((IFacade)persistentClassFacade.getTable()).getTarget());
		assertNotNull(field.get(persistentClassFacade));
	}
	
	@Test 
	public void testIsAbstract() {
		persistentClassTarget.setAbstract(true);
		assertTrue(persistentClassFacade.isAbstract());
		persistentClassTarget.setAbstract(false);
		assertFalse(persistentClassFacade.isAbstract());
	}
	
	@Test
	public void testGetDiscriminator() throws Exception {
		Value valueTarget = createValue();
		Field field = AbstractPersistentClassFacade.class.getDeclaredField("discriminator");
		field.setAccessible(true);
		assertNull(field.get(persistentClassFacade));
		assertNull(persistentClassFacade.getDiscriminator());
		assertNull(field.get(persistentClassFacade));
		((RootClass)persistentClassTarget).setDiscriminator(valueTarget);
		IValue valueFacade = persistentClassFacade.getDiscriminator();
		assertNotNull(valueFacade);
		assertSame(valueFacade, field.get(persistentClassFacade));
		assertSame(valueTarget, ((IFacade)valueFacade).getTarget());
	}
	
	@Test
	public void testGetIdentifier() throws Exception {
		KeyValue valueTarget = createValue();
		Field field = AbstractPersistentClassFacade.class.getDeclaredField("identifier");
		field.setAccessible(true);
		assertNull(field.get(persistentClassFacade));
		assertNull(persistentClassFacade.getIdentifier());
		assertNull(field.get(persistentClassFacade));
		((RootClass)persistentClassTarget).setIdentifier(valueTarget);
		IValue valueFacade = persistentClassFacade.getIdentifier();
		assertNotNull(valueFacade);
		assertSame(valueFacade, field.get(persistentClassFacade));
		assertSame(valueTarget, ((IFacade)valueFacade).getTarget());
	}
	
	@Test
	public void testGetJoinIterator() throws Exception {
		Join join = new Join();
		Field field = AbstractPersistentClassFacade.class.getDeclaredField("joins");
		field.setAccessible(true);
		assertNull(field.get(persistentClassFacade));
		Iterator<IJoin> joinIterator = persistentClassFacade.getJoinIterator();
		assertNotNull(field.get(persistentClassFacade));
		assertTrue(((HashSet<?>)field.get(persistentClassFacade)).isEmpty());
		assertFalse(joinIterator.hasNext());
		field.set(persistentClassFacade, null);
		((RootClass)persistentClassTarget).addJoin(join);
		joinIterator = persistentClassFacade.getJoinIterator();
		assertTrue(joinIterator.hasNext());
		assertSame(join, ((IFacade)joinIterator.next()).getTarget());
		assertFalse(joinIterator.hasNext());
	}
	
	@Test
	public void testGetVersion() throws Exception {
		assertNull(persistentClassFacade.getVersion());
		Property versionTarget = new Property();
		Field field = AbstractPersistentClassFacade.class.getDeclaredField("version");
		field.setAccessible(true);
		assertNull(field.get(persistentClassFacade));
		((RootClass)persistentClassTarget).setVersion(versionTarget);
		IProperty versionFacade = persistentClassFacade.getVersion();
		assertNotNull(versionFacade);
		assertSame(versionFacade, field.get(persistentClassFacade));
		assertSame(versionTarget, ((IFacade)versionFacade).getTarget());
	}
	
	@Test
	public void testSetClassName() {
		assertNull(persistentClassTarget.getClassName());
		persistentClassFacade.setClassName("foo");
		assertEquals("foo", persistentClassTarget.getClassName());
	}
	
	@Test
	public void testSetEntityName() {
		assertNull(persistentClassTarget.getEntityName());
		persistentClassFacade.setEntityName("bar");
		assertEquals("bar", persistentClassTarget.getEntityName());
	}
	
	@Test
	public void testSetDiscriminatorValue() {
		assertNull(persistentClassTarget.getDiscriminatorValue());
		persistentClassFacade.setDiscriminatorValue("foo");
		assertEquals("foo", persistentClassTarget.getDiscriminatorValue());
	}
	
	@Test
	public void testSetAbstract() {
		persistentClassFacade.setAbstract(true);
		assertTrue(persistentClassTarget.isAbstract());
		persistentClassFacade.setAbstract(false);
		assertFalse(persistentClassTarget.isAbstract());
	}
	
	@Test
	public void testAddProperty() throws Exception {
		Field propertiesField = AbstractPersistentClassFacade.class.getDeclaredField("properties");
		propertiesField.setAccessible(true);
		propertiesField.set(persistentClassFacade, new HashMap<String, IProperty>());
		Field propertyClosuresField = AbstractPersistentClassFacade.class.getDeclaredField("propertyClosures");
		propertyClosuresField.setAccessible(true);
		propertyClosuresField.set(persistentClassFacade, new HashSet<IProperty>());
		Property propertyTarget = new Property();
		IProperty propertyFacade = FACADE_FACTORY.createProperty(propertyTarget);
		assertFalse(persistentClassTarget.getPropertyIterator().hasNext());
		assertNull(propertyTarget.getPersistentClass());
		persistentClassFacade.addProperty(propertyFacade);
		assertNull(propertiesField.get(persistentClassFacade));
		assertNull(propertyClosuresField.get(persistentClassFacade));
		assertTrue(persistentClassTarget.getPropertyIterator().hasNext());
		assertSame(propertyTarget, persistentClassTarget.getPropertyIterator().next());
		assertSame(persistentClassTarget, propertyTarget.getPersistentClass());
	}
	
	@Test
	public void testIsInstanceOfJoinedSubclass() {
		assertFalse(persistentClassFacade.isInstanceOfJoinedSubclass());
		JoinedSubclass joinedSubclassTarget = new JoinedSubclass(persistentClassTarget, null);
		IPersistentClass joinedSubclassFacade = new PersistentClassFacadeImpl(FACADE_FACTORY, joinedSubclassTarget);
		assertTrue(joinedSubclassFacade.isInstanceOfJoinedSubclass());
	}
	
	@Test
	public void testSetTable() {
		Table tableTarget = new Table();
		ITable tableFacade = FACADE_FACTORY.createTable(tableTarget);
		assertNull(persistentClassTarget.getTable());
		persistentClassFacade.setTable(tableFacade);
		assertSame(tableTarget, persistentClassTarget.getTable());
	}
	
	@Test
	public void testSetKey() {
		Value valueTarget = createValue();
		IValue valueFacade = FACADE_FACTORY.createValue(valueTarget);
		assertNull(persistentClassTarget.getKey());
		persistentClassFacade.setKey(valueFacade);
		assertSame(valueTarget, persistentClassTarget.getKey());
	}
	
	@Test
	public void testIsInstanceOfSpecialRootClass() {
		assertFalse(persistentClassFacade.isInstanceOfSpecialRootClass());
	}
	
	@Test
	public void testGetParentProperty() {
		try {
			persistentClassFacade.getParentProperty();
			fail();
		} catch (RuntimeException e) {
			assertEquals("getParentProperty() is only allowed on SpecialRootClass", e.getMessage());
		}
	}
	
	private KeyValue createValue() {
		return (KeyValue)Proxy.newProxyInstance(
				getClass().getClassLoader(), 
				new Class[] { KeyValue.class }, 
				new InvocationHandler() {	
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						return null;
					}
		});
	}
	
}
