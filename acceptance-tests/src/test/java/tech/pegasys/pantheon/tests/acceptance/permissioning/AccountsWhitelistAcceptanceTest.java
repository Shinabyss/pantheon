/*
 * Copyright 2018 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package tech.pegasys.pantheon.tests.acceptance.permissioning;

import tech.pegasys.pantheon.tests.acceptance.dsl.AcceptanceTestBase;
import tech.pegasys.pantheon.tests.acceptance.dsl.account.Account;
import tech.pegasys.pantheon.tests.acceptance.dsl.node.Node;
import tech.pegasys.pantheon.tests.acceptance.dsl.node.cluster.Cluster;
import tech.pegasys.pantheon.tests.acceptance.dsl.node.cluster.ClusterConfiguration;
import tech.pegasys.pantheon.tests.acceptance.dsl.node.cluster.ClusterConfigurationBuilder;

import java.util.Collections;

import org.junit.Before;

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
