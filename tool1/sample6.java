public ReferenceEntry<K, V> getLiveEntry(Object key, int hash) {
  if (count != 0) { 
    for (ReferenceEntry<K, V> e = getFirst(hash); e != null;
        e = e.getNext()) {
      if (e.getHash() != hash) {
        continue;
      }

      K entryKey = e.getKey();
      if (entryKey == null) {
        continue;
      }

      if (keyEquivalence.equivalent(key, entryKey)) {
        if (isLive(e)) {
          recordRead(e);
          return e;
        }

        return null;
      }
    }
  }

  return null;
}
