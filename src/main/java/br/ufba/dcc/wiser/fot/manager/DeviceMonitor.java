package br.ufba.dcc.wiser.fot.manager;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hazelcast.core.Cluster;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;

public class DeviceMonitor { 
								
	private HazelcastInstance instance = null;
	Set<Member> listGateway = new HashSet<Member>();

	public void setInstance(HazelcastInstance instance) {
		this.instance = instance;
	}

	public void deviceMonitor() {
		Cluster clusterInst = instance.getCluster();
		//Set<Node> listNodes =  cluster.listNodes();	
		
//		for(Node n : listNodes){
//			System.out.println("---------------------------------------");
//			System.out.println(">>>>>>>>>>>>>>>>>>>>Node: "+n.getHost());
//			Set<String> listGroup = group.listGroupNames(n); 
//			for(String g : listGroup){				
//				System.out.println(">>>>>>>>GroupsNode: "+g.toString());				
//			}	
//			System.out.println("---------------------------------------");
//		}		
		
		try {
			//lista capturada do cellar
			Set<Member> members = clusterInst.getMembers();
			//lista de gateways que caíram
			Set<Member> membersTemp = new HashSet<Member>();
			//gateways que surgiram
			Set<Member> listGatewayTemp = new HashSet<Member>();
			
			membersTemp.addAll(members);

			if (listGateway == null || listGateway.isEmpty()) {
				//Preencheu a listGateway pela primeira vez
				listGateway.addAll(members);
			} else if (!listGateway.equals(members)) {
				listGatewayTemp = new HashSet<Member>();
				
				listGatewayTemp.addAll(listGateway);
				//retorna os gateways que cairam
				listGatewayTemp.removeAll(members);
				//retorna os gateways novos que surgiram
				membersTemp.removeAll(listGateway);
				// atualização da lista principal
				// adiciona novos gateways
				listGateway.addAll(membersTemp); 
				// retirando gateways que sairam do sistema
				for (Member newElement : listGatewayTemp) {
					listGateway.remove(newElement);
				}
				
				//após execução haverá
				//listGateway -- atualizada
				//listGatewayTemp -- com os gateways que caíram
				//membersTemp -- com os gateways que surgiram				
								
			}
		} catch (NullPointerException ex) {
			Logger.getLogger(Example.class.getName()).log(Level.SEVERE, null, ex);
		} catch (UnsupportedOperationException e) {
			Logger.getLogger(Example.class.getName()).log(Level.SEVERE, null, e);
		}

	}

}
