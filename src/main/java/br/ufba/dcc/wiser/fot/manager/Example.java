package br.ufba.dcc.wiser.fot.manager;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Cluster;
import com.hazelcast.core.Member;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashSet;

public class Example {

	private String a;
	private String b;
	private HazelcastInstance instance = null;
	Set<Member> listGateway = new HashSet<Member>();

	public void setA(String a) {
		this.a = a;
	}

	public void setB(String b) {
		this.b = b;
	}

	public void bla() {
		System.out.println("Started Example: " + a + " " + b);
	}

	public void setInstance(HazelcastInstance instance) {
		this.instance = instance;
	}

	public void bli() {
		Cluster cluster = instance.getCluster();

		try {
			Set<Member> members = cluster.getMembers();
			Set<Member> membersTemp = new HashSet<Member>();
			membersTemp.addAll(members);

			if (listGateway == null || listGateway.isEmpty()) {
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>Preencheu a listGateway");
				listGateway.addAll(members);
			} else if (!listGateway.equals(members)) {

				Set<Member> listGatewayTemp = new HashSet<Member>();
				listGatewayTemp.addAll(listGateway);

				listGatewayTemp.removeAll(members);// retorna os gateways que
													// cairam

				membersTemp.removeAll(listGateway);// retorna os gateways novos
													// que surgiram

				// atualização da lista principal
				listGateway.addAll(membersTemp); //adiciona novos gateways
				
				// listGateway.removeAll(listGatewayTemp); //remove gateways que
				// sairam do sistema -- verificar
				for(Member newElement : listGatewayTemp){
					listGateway.remove(newElement);
				}
				System.out.println("\n#####################################\n");
				for (Member newElement : listGateway) {
					System.out.println(">>>>>>>>>>Lista atualizada: " + newElement);
				}
				System.out.println("\n#####################################\n");
			} else {
				System.out.println(">>>>Gateways iguais."); // Nenhuma ação será
															// executada
				System.out.println("\n#####################################\n");
				for (Member newElement : listGateway) {
					System.out.println(">>>>>>>>>>Lista sem alteração: " + newElement);
				}
				System.out.println("\n#####################################\n");
			}
		} catch (NullPointerException ex) {			
			Logger.getLogger(Example.class.getName()).log(Level.SEVERE, null, ex);
		} catch (UnsupportedOperationException e) {			
			Logger.getLogger(Example.class.getName()).log(Level.SEVERE, null, e);
		}

	}

}
