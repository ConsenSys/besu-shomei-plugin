/*
 * Copyright ConsenSys 2023
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package net.consensys.shomei.trielog;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hyperledger.besu.plugin.services.TrieLogService;
import org.hyperledger.besu.plugin.services.trielogs.TrieLogEvent;
import org.hyperledger.besu.plugin.services.trielogs.TrieLogFactory;
import org.hyperledger.besu.plugin.services.trielogs.TrieLogProvider;

public class ZkTrieLogService implements TrieLogService {
  private static ZkTrieLogService singleton = new ZkTrieLogService();

  ZkTrieLogService() {}

  public static ZkTrieLogService getInstance() {
    return singleton;
  }

  // TODO: configure:
  private final AtomicReference<ZkTrieLogObserver> observer = new AtomicReference<>();
  private TrieLogProvider trieLogProvider = null;

  @Override
  public List<TrieLogEvent.TrieLogObserver> getObservers() {
    // initialize observer late so we are assured we have gotten pico cli params from besu
    if (observer.get() == null) {
      observer.compareAndSet(
          null,
          new ZkTrieLogObserver(
              ShomeiCliOptions.INSTANCE.shomeiHttpHost, ShomeiCliOptions.INSTANCE.shomeiHttpPort));
    }
    return List.of(observer.get());
  }

  @Override
  public TrieLogFactory getTrieLogFactory() {
    return ZkTrieLogFactory.INSTANCE;
  }

  @Override
  public void configureTrieLogProvider(final TrieLogProvider trieLogProvider) {
    this.trieLogProvider = trieLogProvider;
  }

  @Override
  public TrieLogProvider getTrieLogProvider() {
    return trieLogProvider;
  }
}
