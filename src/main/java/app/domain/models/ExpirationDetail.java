package app.domain.models;

import java.time.LocalDateTime;

public class ExpirationDetail {

    private String reason;
    private LocalDateTime expirationDateTime;
    private String creatorUserId;

    // ── Constructor ───────────────────────────────────────────────────

    public ExpirationDetail(String creatorUserId) {
        this.reason = "No approval received within the 60 minute time limit.";
        this.expirationDateTime = LocalDateTime.now();
        this.creatorUserId = creatorUserId;
    }

    // ── Getters & Setters ─────────────────────────────────────────────

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getExpirationDateTime() { return expirationDateTime; }
    public void setExpirationDateTime(LocalDateTime expirationDateTime) { this.expirationDateTime = expirationDateTime; }

    public String getCreatorUserId() { return creatorUserId; }
    public void setCreatorUserId(String creatorUserId) { this.creatorUserId = creatorUserId; }

    @Override
    public String toString() {
        return "ExpirationDetail{reason='" + reason +
               "', expirationDateTime=" + expirationDateTime +
               ", creatorUserId='" + creatorUserId + "'}";
    }
}