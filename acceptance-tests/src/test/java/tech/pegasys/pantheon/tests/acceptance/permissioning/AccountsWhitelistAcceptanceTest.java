package tech.pegasys.pantheon.tests.acceptance.permissioning;

import java.util.Collections;
import tech.pegasys.pantheon.tests.acceptance.dsl.AcceptanceTestBase;
import tech.pegasys.pantheon.tests.acceptance.dsl.account.Account;
import tech.pegasys.pantheon.tests.acceptance.dsl.node.Node;
import tech.pegasys.pantheon.tests.acceptance.dsl.node.cluster.Cluster;
import tech.pegasys.pantheon.tests.acceptance.dsl.node.cluster.ClusterConfiguration;
import tech.pegasys.pantheon.tests.acceptance.dsl.node.cluster.ClusterConfigurationBuilder;

import org.junit.Before;
import org.junit.Test;

public class AccountsWhitelistAcceptanceTest extends AcceptanceTestBase {

  private Cluster permissionedCluster;
  private Account forbiddenAccount;
  private Account allowedAccount;
  private Node permissionedNode;
  private Node minerNode;

  @Before
  public void setUp() throws Exception {
    final ClusterConfiguration clusterConfiguration =
        new ClusterConfigurationBuilder().setAwaitPeerDiscovery(false).build();

    permissionedCluster = new Cluster(clusterConfiguration, net);
    minerNode = pantheon.createMinerNode("minerNode");
    forbiddenAccount = accounts.createAccount("forbiddenaAccount");
    allowedAccount = accounts.createAccount("allowedAccount");
    permissionedNode =
        pantheon.createNodeWithAccountsWhitelist(
            "permissioned-node", Collections.singletonList(allowedAccount));
    permissionedCluster.start(minerNode, permissionedNode);
  }



  @Override
  public void tearDownAcceptanceTestBase() {
    permissionedCluster.stop();
    super.tearDownAcceptanceTestBase();
  }

}
