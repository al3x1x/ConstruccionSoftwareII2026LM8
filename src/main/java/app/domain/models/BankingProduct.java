package app.domain.models;

import app.domain.enums.ProductCategory;

public class BankingProduct {

    private String productCode;
    private String productName;
    private ProductCategory category;
    private boolean requiresApproval;

    // ── Constructor ───────────────────────────────────────────────────

    public BankingProduct(String productCode, String productName,
                           ProductCategory category, boolean requiresApproval) {
        this.productCode = productCode;
        this.productName = productName;
        this.category = category;
        this.requiresApproval = requiresApproval;
    }

    // ── Getters & Setters ─────────────────────────────────────────────

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public ProductCategory getCategory() { return category; }
    public void setCategory(ProductCategory category) { this.category = category; }

    public boolean isRequiresApproval() { return requiresApproval; }
    public void setRequiresApproval(boolean requiresApproval) { this.requiresApproval = requiresApproval; }

    @Override
    public String toString() {
        return "BankingProduct{code='" + productCode + "', name='" + productName +
               "', category=" + category + ", requiresApproval=" + requiresApproval + "}";
    }
}