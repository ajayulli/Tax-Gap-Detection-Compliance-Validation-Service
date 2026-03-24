-- 1. Insert Pre-filled User for Spring Security Authentication
-- The {noop} prefix tells Spring Security we are storing the password in plain text for this assignment.
INSERT INTO users (username, password, role)
VALUES ('admin', '{noop}password', 'ROLE_ADMIN');

-- 2. Insert Dynamic Tax Rules (JSON Configurations)
-- Rule A: Flags any transaction over $10,000
INSERT INTO tax_rule (rule_name, is_active, configuration)
VALUES ('HIGH_VALUE_TRANSACTION', true, '{"threshold": 10000}');

-- Rule B: Flags refunds larger than $5,000 for manual review
INSERT INTO tax_rule (rule_name, is_active, configuration)
VALUES ('REFUND_VALIDATION', true, '{"maxRefundLimit": 5000}');

-- Rule C: Flags transactions over $50,000 if their tax rate is less than 18%
INSERT INTO tax_rule (rule_name, is_active, configuration)
VALUES ('GST_SLAB_VIOLATION', true, '{"slabThreshold": 50000, "requiredRate": 0.18}');