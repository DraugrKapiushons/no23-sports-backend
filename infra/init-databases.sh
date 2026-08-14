#!/bin/bash
# Runs automatically on first container start (mounted into
# /docker-entrypoint-initdb.d/). Creates one database per service, all
# owned by the same shared POSTGRES_USER - keeps this a "database per
# service" layout logically, without needing a separate Postgres
# container (and separate port) per service.
set -euo pipefail

DATABASES=(
  no23_sports_user
  no23_sports_lessons
  no23_sports_user_profile
  no23_menu
  no23_subscription
  no23_mealplan
  no23_payment
  no23_success_stories
  no23_membership
  no23_instructors
  no23_blog
  no23_faq
  no23_reservations
  no23_calorie_tracking
)

for db in "${DATABASES[@]}"; do
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    SELECT 'CREATE DATABASE $db OWNER $POSTGRES_USER'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$db')\gexec
EOSQL
done
