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
package tech.pegasys.pantheon.ethereum.jsonrpc.internal.methods;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import tech.pegasys.pantheon.ethereum.jsonrpc.MockPeerConnection;
import tech.pegasys.pantheon.ethereum.jsonrpc.internal.JsonRpcRequest;
import tech.pegasys.pantheon.ethereum.jsonrpc.internal.response.JsonRpcResponse;
import tech.pegasys.pantheon.ethereum.jsonrpc.internal.response.JsonRpcSuccessResponse;
import tech.pegasys.pantheon.ethereum.jsonrpc.internal.results.PeerResult;
import tech.pegasys.pantheon.ethereum.p2p.api.P2PNetwork;
import tech.pegasys.pantheon.ethereum.p2p.api.PeerConnection;
import tech.pegasys.pantheon.ethereum.p2p.wire.PeerInfo;
import tech.pegasys.pantheon.util.bytes.BytesValue;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AdminPeersTest {

  private AdminPeers adminPeers;

  @Mock private P2PNetwork p2pNetwork;

  @Before
  public void before() {
    adminPeers = new AdminPeers(p2pNetwork);
  }

  @Test
  public void shouldReturnExpectedMethodName() {
    assertThat(adminPeers.getName()).isEqualTo("admin_peers");
  }

  @Test
  public void shouldReturnEmptyPeersListWhenP2PNetworkDoesNotHavePeers() {
    final JsonRpcResponse expectedResponse =
        new JsonRpcSuccessResponse(null, Collections.emptyList());
    final JsonRpcRequest request = adminPeers();
    when(p2pNetwork.getPeers()).thenReturn(Collections.emptyList());

    final JsonRpcResponse response = adminPeers.response(request);

    assertThat(response).isEqualToComparingFieldByField(expectedResponse);
  }

  @Test
  public void shouldReturnExpectedPeerListWhenP2PNetworkHavePeers() {
    final Collection<PeerConnection> peerList = peerList();
    final List<PeerResult> expectedPeerResults =
        peerList.stream().map(PeerResult::new).collect(Collectors.toList());

    final JsonRpcRequest request = adminPeers();
    final JsonRpcResponse expectedResponse = new JsonRpcSuccessResponse(null, expectedPeerResults);

    when(p2pNetwork.getPeers()).thenReturn(peerList);

    final JsonRpcResponse response = adminPeers.response(request);

    assertThat(response).isEqualToComparingFieldByFieldRecursively(expectedResponse);
  }

  private Collection<PeerConnection> peerList() {
    final PeerInfo peerInfo =
        new PeerInfo(5, "0x0", Collections.emptyList(), 30303, BytesValue.EMPTY);
    final PeerConnection p =
        new MockPeerConnection(
            peerInfo,
            InetSocketAddress.createUnresolved("1.2.3.4", 9876),
            InetSocketAddress.createUnresolved("4.3.2.1", 6789));
    return Lists.newArrayList(p);
  }

  private JsonRpcRequest adminPeers() {
    return new JsonRpcRequest("2.0", "admin_peers", new Object[] {});
  }
}
