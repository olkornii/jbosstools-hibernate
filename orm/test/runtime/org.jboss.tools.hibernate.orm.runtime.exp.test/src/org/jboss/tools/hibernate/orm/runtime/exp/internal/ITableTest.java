package org.jboss.tools.hibernate.orm.runtime.exp.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.Iterator;

import org.hibernate.mapping.BasicValue;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.KeyValue;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mapping.Table;
import org.hibernate.tool.orm.jbt.util.DummyMetadataBuildingContext;
import org.jboss.tools.hibernate.orm.runtime.exp.internal.util.NewFacadeFactory;
import org.jboss.tools.hibernate.runtime.common.IFacade;
import org.jboss.tools.hibernate.runtime.spi.IColumn;
import org.jboss.tools.hibernate.runtime.spi.IPrimaryKey;
import org.jboss.tools.hibernate.runtime.spi.ITable;
import org.jboss.tools.hibernate.runtime.spi.IValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ITableTest {
	
	private ITable tableFacade = null;
	private Table tableTarget = null;
	
	@BeforeEach
	public void beforeEach() {
		tableFacade = NewFacadeFactory.INSTANCE.createTable("foo");
		tableTarget = (Table)((IFacade)tableFacade).getTarget();
	}
	
	@Test
	public void testConstruction() {
		assertNotNull(tableFacade);
		assertNotNull(tableTarget);
	}
	

	@Test
	public void testGetName() {
		assertEquals("foo", tableFacade.getName());
		tableTarget.setName("bar");
		assertEquals("bar", tableFacade.getName());
	}
	
	@Test
	public void testAddColumn() {
		IColumn columnFacade = NewFacadeFactory.INSTANCE.createColumn("foo");
		Column columnTarget = (Column)((IFacade)columnFacade).getTarget();
		assertNull(tableTarget.getColumn(columnTarget));
		tableFacade.addColumn(columnFacade);
		assertSame(columnTarget, tableTarget.getColumn(columnTarget));
	}
	
	@Test
	public void testGetCatalog() {
		assertNull(tableFacade.getCatalog());
		tableTarget.setCatalog("foo");
		assertEquals("foo", tableFacade.getCatalog());
	}
	
	@Test
	public void testGetSchema() {
		assertNull(tableFacade.getSchema());
		tableTarget.setSchema("foo");
		assertEquals("foo", tableFacade.getSchema());
	}
	
	@Test
	public void testGetPrimaryKey() {
		PrimaryKey primaryKeyTarget = new PrimaryKey(tableTarget);
		IPrimaryKey primaryKeyFacade = tableFacade.getPrimaryKey();
		assertNotSame(primaryKeyTarget, ((IFacade)primaryKeyFacade).getTarget());
		tableTarget.setPrimaryKey(primaryKeyTarget);
		primaryKeyFacade = tableFacade.getPrimaryKey();
		assertSame(primaryKeyTarget, ((IFacade)primaryKeyFacade).getTarget());
	}
	
	@Test
	public void testGetColumnIterator() {
		Iterator<IColumn> columnIterator = tableFacade.getColumnIterator();
		assertFalse(columnIterator.hasNext());
		IColumn columnFacade1 = NewFacadeFactory.INSTANCE.createColumn("bar");
		tableFacade.addColumn(columnFacade1);
		columnIterator = tableFacade.getColumnIterator();
		IColumn columnFacade2 = columnIterator.next();
		assertEquals(columnFacade1, columnFacade2);
	}
	
	@Test
	public void testGetComment() {
		assertNull(tableFacade.getComment());
		tableTarget.setComment("foo");
		assertEquals("foo", tableFacade.getComment());
	}
	
	@Test
	public void testGetRowId() {
		assertNull(tableFacade.getRowId());
		tableTarget.setRowId("foo");
		assertEquals("foo", tableFacade.getRowId());
	}
	
	@Test
	public void testGetSubselect() {
		assertNull(tableFacade.getSubselect());		
		tableTarget.setSubselect("foo");
		assertEquals("foo", tableFacade.getSubselect());
	}
	
	@Test
	public void testHasDenormalizedTables() throws Exception {
		assertFalse(tableFacade.hasDenormalizedTables());
		Method method = Table.class.getDeclaredMethod(
				"setHasDenormalizedTables", 
				new Class[] { });
		method.setAccessible(true);
		method.invoke(tableTarget, new Object[] { });
		assertTrue(tableFacade.hasDenormalizedTables());
	}
	
	@Test
	public void testIsAbstract() {
		tableTarget.setAbstract(true);
		assertTrue(tableFacade.isAbstract());		
		tableTarget.setAbstract(false);
		assertFalse(tableFacade.isAbstract());		
	}
	
	@Test
	public void testIsAbstractUnionTable() throws Exception {
		tableTarget.setAbstract(false);
		assertFalse(tableFacade.isAbstractUnionTable());	
		tableTarget.setAbstract(true);
		assertFalse(tableFacade.isAbstractUnionTable());	
		Method method = Table.class.getDeclaredMethod(
				"setHasDenormalizedTables", 
				new Class[] { });
		method.setAccessible(true);
		method.invoke(tableTarget, new Object[] { });
		assertTrue(tableFacade.isAbstractUnionTable());
	}
	
	@Test
	public void testIsPhysicalTable() {
		tableTarget.setSubselect("foo");
		assertFalse(tableFacade.isPhysicalTable());	
		tableTarget.setSubselect(null);
		assertTrue(tableFacade.isPhysicalTable());
	}
	
	@Test
	public void testGetIdentifierValue() {
		IValue valueFacade = tableFacade.getIdentifierValue();
		assertNull(valueFacade);
		KeyValue value = new BasicValue(DummyMetadataBuildingContext.INSTANCE);
		tableTarget.setIdentifierValue(value);
		valueFacade = tableFacade.getIdentifierValue();
		assertSame(value, ((IFacade)valueFacade).getTarget());
	}
	
}
