package com.nec.strudel.workload.cluster.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.nec.congenio.ConfigValue;
import com.nec.congenio.Values;
import com.nec.strudel.workload.cluster.Cluster;
import com.nec.strudel.workload.cluster.Node;
import com.nec.strudel.workload.test.ClusterFiles;
import com.nec.strudel.workload.test.Resources;

public class ClusterTest {


	@Test
	public void testCluster() {
		Cluster cc = Resources.create(ClusterFiles.CLUSTER000);
		assertFalse(cc.isLocal());
		assertEquals(6, cc.size());
		Node n = cc.nodes()[0];
		assertEquals(6, n.getNum());
		Cluster cc4 = cc.limit(4);
		assertEquals(4, cc4.size());
		Node n4 = cc4.nodes()[0];
		assertEquals(4, n4.getNum());
	}
	@Test
	public void testNode() {
		Cluster cc = Resources.create(ClusterFiles.CLUSTER000);
		Node n = cc.nodes()[1];
		ConfigValue conf = Values.builder("test")
				.add("node", n).build();
		Node n1 = conf.getObject("node", Node.class);
		assertEquals(n.getId(), n1.getId());
		assertEquals(n.getUrl(), n1.getUrl());
	}
	@Test
	public void testNodeCreate() {
		Cluster cc = Resources.create(ClusterFiles.CLUSTER000);
		Node n = cc.nodes()[1];
		ConfigValue conf = Values.create(n);
		Node n1 = conf.toObject(Node.class);
		assertEquals(n.getId(), n1.getId());
		assertEquals(n.getUrl(), n1.getUrl());
	}
	@Test
	public void testCluster1() {
		Cluster cc = Resources.create(ClusterFiles.CLUSTER001);
		assertEquals(8, cc.size());
		Node n = cc.nodes()[0];
		assertEquals(8, n.getNum());
		Cluster cc4 = cc.limit(4);
		assertEquals(4, cc4.size());
		Node n4 = cc4.nodes()[0];
		assertEquals(4, n4.getNum());
	}
}
