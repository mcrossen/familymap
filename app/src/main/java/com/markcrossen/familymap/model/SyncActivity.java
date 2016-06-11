package com.markcrossen.familymap.model;

public interface SyncActivity {
    void SyncError(Exception error);
    void SyncDone();
}
