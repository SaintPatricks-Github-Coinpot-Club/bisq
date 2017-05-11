/*
 * This file is part of bisq.
 *
 * bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package io.bisq.network.p2p.storage;

import io.bisq.common.persistence.Persistable;
import io.bisq.generated.protobuffer.PB;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * This class was not generalized to HashMapPersistable (like we did with #ListPersistable) because
 * in protobuffer the map construct can't be anything, so the straightforward mapping was not possible.
 * Hence this Persistable class.
 */
public class SequenceNumberMap implements Persistable {
    @Delegate(excludes = ExcludeFromDelegate.class)
    @Getter
    @Setter
    private HashMap<P2PDataStorage.ByteArray, P2PDataStorage.MapValue> hashMap = new HashMap<>();

    public SequenceNumberMap() {
    }

    public SequenceNumberMap(SequenceNumberMap sequenceNumberMap) {
        this.hashMap = sequenceNumberMap.getHashMap();
    }

    public SequenceNumberMap(HashMap<P2PDataStorage.ByteArray, P2PDataStorage.MapValue> hashMap) {
        this.hashMap = hashMap;
    }

    @Override
    public PB.Persistable toProtoMessage() {
        return PB.Persistable.newBuilder().setSequenceNumberMap(
                PB.SequenceNumberMap.newBuilder().addAllSequenceNumberEntries(
                        hashMap.entrySet().stream()
                                .map(entry ->
                                        PB.SequenceNumberEntry.newBuilder().setBytes(entry.getKey().toProtoMessage())
                                                .setMapValue(entry.getValue().toProtoMessage()).build())
                                .collect(Collectors.toList()))).build();
    }

    public static SequenceNumberMap fromProto(PB.SequenceNumberMap sequenceNumberMap) {
        List<PB.SequenceNumberEntry> sequenceNumberEntryList = sequenceNumberMap.getSequenceNumberEntriesList();
        HashMap<P2PDataStorage.ByteArray, P2PDataStorage.MapValue> result = new HashMap<>();
        for (final PB.SequenceNumberEntry entry : sequenceNumberEntryList) {
            result.put(P2PDataStorage.ByteArray.fromProto(entry.getBytes()), P2PDataStorage.MapValue.fromProto(entry.getMapValue()));
        }
        return new SequenceNumberMap(result);
    }

    // avoid warnings in IDE, because of type erasure intellij thinks there are duplicate methods generated by lombok delegate
    private interface ExcludeFromDelegate<K,V> {
        public void forEach(BiConsumer<? super P2PDataStorage.ByteArray, ? super P2PDataStorage.MapValue> action);
        public void putAll(Map<? extends P2PDataStorage.ByteArray, ? extends P2PDataStorage.MapValue> all);
        public void replaceAll(BiFunction<? super P2PDataStorage.ByteArray, ? super P2PDataStorage.MapValue, ? extends P2PDataStorage.MapValue> all);
    }
}
