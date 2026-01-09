--
-- NOTE:
--
-- File paths need to be edited. Search for /backups and
-- replace it with the path to the directory containing
-- the extracted data files.
--
--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.9
-- Dumped by pg_dump version 13.7 (Debian 13.7-0+deb11u1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE venlab;
--
-- Name: venlab; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE venlab WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'German_Austria.1252';


ALTER DATABASE venlab OWNER TO postgres;

\connect venlab

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: backup; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA backup;


ALTER SCHEMA backup OWNER TO postgres;

--
-- Name: venlab; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA venlab;


ALTER SCHEMA venlab OWNER TO postgres;

--
-- Name: find_all_modified_analysis(); Type: FUNCTION; Schema: venlab; Owner: postgres
--

CREATE FUNCTION venlab.find_all_modified_analysis() RETURNS TABLE(a_id bigint, s_id character varying, s_stamp timestamp without time zone, pol numeric, nat numeric, kal numeric, an numeric, glu numeric, dry numeric, date_in timestamp without time zone, date_out timestamp without time zone, weight_mea numeric, weight_nrm numeric, weight_cur numeric, weight_dif numeric, density numeric, a_flags character varying, lane integer, comment character varying, date_exported timestamp without time zone)
    LANGUAGE plpgsql
    AS $$
BEGIN
RETURN QUERY
 SELECT * FROM venlab.analysis a WHERE a.date_exported = timestamp '2000-01-01 00:00:00';
END;
$$;


ALTER FUNCTION venlab.find_all_modified_analysis() OWNER TO postgres;

--
-- Name: find_all_modified_box(); Type: FUNCTION; Schema: venlab; Owner: postgres
--

CREATE FUNCTION venlab.find_all_modified_box() RETURNS TABLE(b_id character varying, name character varying, num_max integer, type integer, comment character varying, date_exported timestamp without time zone)
    LANGUAGE plpgsql
    AS $$
BEGIN
RETURN QUERY
 SELECT * FROM venlab.box b WHERE b.date_exported = timestamp '2000-01-01 00:00:00';
END;
$$;


ALTER FUNCTION venlab.find_all_modified_box() OWNER TO postgres;

--
-- Name: find_all_modified_boxpos(); Type: FUNCTION; Schema: venlab; Owner: postgres
--

CREATE FUNCTION venlab.find_all_modified_boxpos() RETURNS TABLE(b_id character varying, bpos_id integer, s_id character varying, s_stamp timestamp without time zone, date_exported timestamp without time zone)
    LANGUAGE plpgsql
    AS $$
BEGIN
RETURN QUERY
 SELECT * FROM venlab.boxpos b WHERE b.date_exported = timestamp '2000-01-01 00:00:00';
END;
$$;


ALTER FUNCTION venlab.find_all_modified_boxpos() OWNER TO postgres;

--
-- Name: find_all_modified_log(); Type: FUNCTION; Schema: venlab; Owner: postgres
--

CREATE FUNCTION venlab.find_all_modified_log() RETURNS TABLE(log_id bigint, date_created timestamp without time zone, level character varying, info character varying, s_id character varying, s_stamp timestamp without time zone, a_id bigint, date_exported timestamp without time zone)
    LANGUAGE plpgsql
    AS $$
BEGIN
RETURN QUERY
 SELECT * FROM venlab.log l WHERE l.date_exported = timestamp '2000-01-01 00:00:00';
END;
$$;


ALTER FUNCTION venlab.find_all_modified_log() OWNER TO postgres;

--
-- Name: find_all_modified_sample(); Type: FUNCTION; Schema: venlab; Owner: postgres
--

CREATE FUNCTION venlab.find_all_modified_sample() RETURNS TABLE(s_id character varying, s_stamp timestamp without time zone, name character varying, weight_net numeric, weight_bru numeric, weight_tar numeric, quantity integer, distance numeric, date_crumbled timestamp without time zone, s_flags character varying, lane integer, comment character varying, date_exported timestamp without time zone, boxpos character varying)
    LANGUAGE plpgsql
    AS $$
BEGIN
RETURN QUERY
    SELECT *,( SELECT venlab.get_box_positions(s.s_id,s.s_stamp) as boxpos ) FROM venlab.sample s WHERE s.date_exported = timestamp '2000-01-01 00:00:00';
END;
$$;


ALTER FUNCTION venlab.find_all_modified_sample() OWNER TO postgres;

--
-- Name: find_all_not_exported_analysis(); Type: FUNCTION; Schema: venlab; Owner: postgres
--

CREATE FUNCTION venlab.find_all_not_exported_analysis() RETURNS TABLE(a_id bigint, s_id character varying, s_stamp timestamp without time zone, pol numeric, nat numeric, kal numeric, an numeric, glu numeric, dry numeric, date_in timestamp without time zone, date_out timestamp without time zone, weight_mea numeric, weight_nrm numeric, weight_cur numeric, weight_dif numeric, density numeric, a_flags character varying, lane integer, comment character varying, date_exported timestamp without time zone)
    LANGUAGE plpgsql
    AS $$
BEGIN
RETURN QUERY
 SELECT * FROM venlab.analysis a WHERE a.date_exported IS NULL;
END;
$$;


ALTER FUNCTION venlab.find_all_not_exported_analysis() OWNER TO postgres;

--
-- Name: find_all_not_exported_box(); Type: FUNCTION; Schema: venlab; Owner: postgres
--

CREATE FUNCTION venlab.find_all_not_exported_box() RETURNS TABLE(b_id character varying, name character varying, num_max integer, type integer, comment character varying, date_exported timestamp without time zone)
    LANGUAGE plpgsql
    AS $$
BEGIN
RETURN QUERY
 SELECT * FROM venlab.box b WHERE b.date_exported IS NULL;
END;
$$;


ALTER FUNCTION venlab.find_all_not_exported_box() OWNER TO postgres;

--
-- Name: find_all_not_exported_boxpos(); Type: FUNCTION; Schema: venlab; Owner: postgres
--

CREATE FUNCTION venlab.find_all_not_exported_boxpos() RETURNS TABLE(b_id character varying, bpos_id integer, s_id character varying, s_stamp timestamp without time zone, date_exported timestamp without time zone)
    LANGUAGE plpgsql
    AS $$
BEGIN
RETURN QUERY
 SELECT * FROM venlab.boxpos b WHERE b.date_exported IS NULL;
END;
$$;


ALTER FUNCTION venlab.find_all_not_exported_boxpos() OWNER TO postgres;

--
-- Name: find_all_not_exported_log(); Type: FUNCTION; Schema: venlab; Owner: postgres
--

CREATE FUNCTION venlab.find_all_not_exported_log() RETURNS TABLE(log_id bigint, date_created timestamp without time zone, level character varying, info character varying, s_id character varying, s_stamp timestamp without time zone, a_id bigint, date_exported timestamp without time zone)
    LANGUAGE plpgsql
    AS $$
BEGIN
RETURN QUERY
 SELECT * FROM venlab.log l WHERE l.date_exported IS NULL;
END;
$$;


ALTER FUNCTION venlab.find_all_not_exported_log() OWNER TO postgres;

--
-- Name: find_all_not_exported_sample(); Type: FUNCTION; Schema: venlab; Owner: postgres
--

CREATE FUNCTION venlab.find_all_not_exported_sample() RETURNS TABLE(s_id character varying, s_stamp timestamp without time zone, name character varying, weight_net numeric, weight_bru numeric, weight_tar numeric, quantity integer, distance numeric, date_crumbled timestamp without time zone, s_flags character varying, lane integer, comment character varying, date_exported timestamp without time zone, boxpos character varying)
    LANGUAGE plpgsql
    AS $$
BEGIN
RETURN QUERY
 SELECT *,( SELECT venlab.get_box_positions(s.s_id,s.s_stamp) as boxpos ) FROM venlab.sample s WHERE s.date_exported IS NULL;
END;
$$;


ALTER FUNCTION venlab.find_all_not_exported_sample() OWNER TO postgres;

--
-- Name: find_all_not_plausible_analysis(); Type: FUNCTION; Schema: venlab; Owner: postgres
--

CREATE FUNCTION venlab.find_all_not_plausible_analysis() RETURNS TABLE(a_id bigint, s_id character varying, s_stamp timestamp without time zone, pol numeric, nat numeric, kal numeric, an numeric, glu numeric, dry numeric, date_in timestamp without time zone, date_out timestamp without time zone, weight_mea numeric, weight_nrm numeric, weight_cur numeric, weight_dif numeric, density numeric, a_flags character varying, lane integer, comment character varying, date_exported timestamp without time zone)
    LANGUAGE plpgsql
    AS $$
BEGIN
RETURN QUERY
SELECT * FROM venlab.analysis a WHERE a.date_in IS NULL AND a.date_out IS NOT NULL AND (a.date_exported IS NULL OR a.date_exported = (timestamp '2000-01-01 00:00:00'));
END;
$$;


ALTER FUNCTION venlab.find_all_not_plausible_analysis() OWNER TO postgres;

--
-- Name: get_box_positions(character varying, timestamp without time zone); Type: FUNCTION; Schema: venlab; Owner: postgres
--

CREATE FUNCTION venlab.get_box_positions(sid character varying, sstamp timestamp without time zone) RETURNS character varying
    LANGUAGE plpgsql STABLE
    AS $$
DECLARE
    bid text;
    bpos text;
    count int;
BEGIN
SELECT venlab.boxpos.b_id, to_char(venlab.boxpos.bpos_id,'FM00') INTO bid, bpos FROM venlab.boxpos WHERE venlab.boxpos.s_stamp = sstamp AND venlab.boxpos.s_id = sid;
SELECT count(*) INTO count FROM venlab.boxpos WHERE venlab.boxpos.s_stamp = sstamp AND venlab.boxpos.s_id = sid;
IF count > 1 THEN
RETURN concat(bid, '!', bpos);
ELSIF count = 1 THEN
RETURN concat(bid, '/', bpos);
ELSE
RETURN '';
END IF;
END;
$$;


ALTER FUNCTION venlab.get_box_positions(sid character varying, sstamp timestamp without time zone) OWNER TO postgres;

--
-- Name: get_box_positions(character varying, timestamp with time zone); Type: FUNCTION; Schema: venlab; Owner: postgres
--

CREATE FUNCTION venlab.get_box_positions(sid character varying, sstamp timestamp with time zone) RETURNS character varying
    LANGUAGE plpgsql STABLE
    AS $$
DECLARE
    bid text;
    bpos text;
    count int;
BEGIN
SELECT venlab.boxpos.b_id, to_char(venlab.boxpos.bpos_id,'FM00') INTO bid, bpos FROM venlab.boxpos WHERE venlab.boxpos.s_stamp = sstamp AND venlab.boxpos.s_id = sid;
SELECT count(*) INTO count FROM venlab.boxpos WHERE venlab.boxpos.s_stamp = sstamp AND venlab.boxpos.s_id = sid;
IF count > 1 THEN
RETURN concat(bid, '!', bpos);
ELSIF count = 1 THEN
RETURN concat(bid, '/', bpos);
ELSE
RETURN '';
END IF;
END;
$$;


ALTER FUNCTION venlab.get_box_positions(sid character varying, sstamp timestamp with time zone) OWNER TO postgres;

SET default_tablespace = '';

--
-- Name: analysis; Type: TABLE; Schema: backup; Owner: postgres
--

CREATE TABLE backup.analysis (
    a_id bigint,
    s_id character varying(13),
    s_stamp timestamp without time zone,
    pol numeric(8,2),
    nat numeric(8,2),
    kal numeric(8,2),
    an numeric(8,2),
    dry numeric(8,2),
    date_in timestamp without time zone,
    date_out timestamp without time zone,
    weight_mea numeric(8,2),
    weight_nrm numeric(8,2),
    weight_cur numeric(8,2),
    weight_dif numeric(8,2),
    density numeric(8,2),
    a_flags character varying(15),
    lane integer,
    comment character varying(255),
    date_exported timestamp without time zone
);


ALTER TABLE backup.analysis OWNER TO postgres;

--
-- Name: sample; Type: TABLE; Schema: backup; Owner: postgres
--

CREATE TABLE backup.sample (
    s_id character varying(13),
    s_stamp timestamp without time zone,
    name character varying(255),
    weight_net numeric(8,2),
    weight_bru numeric(8,2),
    weight_tar numeric(8,2),
    quantity integer,
    distance numeric(8,2),
    date_crumbled timestamp without time zone,
    s_flags character varying(10),
    lane integer,
    comment character varying(255),
    date_exported timestamp without time zone
);


ALTER TABLE backup.sample OWNER TO postgres;

--
-- Name: analysis; Type: TABLE; Schema: venlab; Owner: postgres
--

CREATE TABLE venlab.analysis (
    a_id bigint NOT NULL,
    s_id character varying(13),
    s_stamp timestamp without time zone,
    pol numeric(8,2),
    nat numeric(8,2),
    kal numeric(8,2),
    an numeric(8,2),
    glu numeric(8,2),
    dry numeric(8,2),
    date_in timestamp without time zone,
    date_out timestamp without time zone,
    weight_mea numeric(8,2),
    weight_nrm numeric(8,2),
    weight_cur numeric(8,2),
    weight_dif numeric(8,2),
    density numeric(8,2),
    a_flags character varying(15) DEFAULT '----------'::character varying,
    lane integer DEFAULT 1,
    comment character varying(255),
    date_exported timestamp without time zone
);


ALTER TABLE venlab.analysis OWNER TO postgres;

--
-- Name: analysis_a_id_seq; Type: SEQUENCE; Schema: venlab; Owner: postgres
--

CREATE SEQUENCE venlab.analysis_a_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE venlab.analysis_a_id_seq OWNER TO postgres;

--
-- Name: analysis_a_id_seq; Type: SEQUENCE OWNED BY; Schema: venlab; Owner: postgres
--

ALTER SEQUENCE venlab.analysis_a_id_seq OWNED BY venlab.analysis.a_id;


--
-- Name: box; Type: TABLE; Schema: venlab; Owner: postgres
--

CREATE TABLE venlab.box (
    b_id character varying(4) NOT NULL,
    name character varying(255),
    num_max integer DEFAULT 40,
    type integer DEFAULT 1,
    comment character varying(255),
    date_exported timestamp without time zone,
    CONSTRAINT con_box_num_max CHECK (((num_max > 0) AND (num_max < 1000))),
    CONSTRAINT con_box_type CHECK (((type > 0) AND (type < 10)))
);


ALTER TABLE venlab.box OWNER TO postgres;

--
-- Name: boxpos; Type: TABLE; Schema: venlab; Owner: postgres
--

CREATE TABLE venlab.boxpos (
    b_id character varying(4) NOT NULL,
    bpos_id integer NOT NULL,
    s_id character varying(13),
    s_stamp timestamp without time zone,
    date_exported timestamp without time zone
);


ALTER TABLE venlab.boxpos OWNER TO postgres;

--
-- Name: log; Type: TABLE; Schema: venlab; Owner: postgres
--

CREATE TABLE venlab.log (
    log_id bigint NOT NULL,
    date_created timestamp without time zone DEFAULT now(),
    level character varying(10),
    info character varying(255),
    s_id character varying(13),
    s_stamp timestamp without time zone,
    a_id bigint,
    date_exported timestamp without time zone
);


ALTER TABLE venlab.log OWNER TO postgres;

--
-- Name: log_log_id_seq; Type: SEQUENCE; Schema: venlab; Owner: postgres
--

CREATE SEQUENCE venlab.log_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE venlab.log_log_id_seq OWNER TO postgres;

--
-- Name: log_log_id_seq; Type: SEQUENCE OWNED BY; Schema: venlab; Owner: postgres
--

ALTER SEQUENCE venlab.log_log_id_seq OWNED BY venlab.log.log_id;


--
-- Name: sample; Type: TABLE; Schema: venlab; Owner: postgres
--

CREATE TABLE venlab.sample (
    s_id character varying(13) NOT NULL,
    s_stamp timestamp without time zone NOT NULL,
    name character varying(255),
    weight_net numeric(8,2),
    weight_bru numeric(8,2),
    weight_tar numeric(8,2),
    quantity integer,
    distance numeric(8,2),
    date_crumbled timestamp without time zone,
    s_flags character varying(10) DEFAULT '-----'::character varying,
    lane integer,
    comment character varying(255),
    date_exported timestamp without time zone
);


ALTER TABLE venlab.sample OWNER TO postgres;

--
-- Name: sample_boxpos; Type: VIEW; Schema: venlab; Owner: postgres
--

CREATE VIEW venlab.sample_boxpos AS
 SELECT sample.s_id,
    sample.s_stamp,
    ( SELECT venlab.get_box_positions(sample.s_id, sample.s_stamp) AS boxpos) AS boxpos
   FROM venlab.sample;


ALTER TABLE venlab.sample_boxpos OWNER TO postgres;

--
-- Name: threshold; Type: TABLE; Schema: venlab; Owner: postgres
--

CREATE TABLE venlab.threshold (
    th_id character varying(10) NOT NULL,
    value_min numeric(8,2),
    value_max numeric(8,2),
    date_changed timestamp without time zone DEFAULT now()
);


ALTER TABLE venlab.threshold OWNER TO postgres;

--
-- Name: analysis a_id; Type: DEFAULT; Schema: venlab; Owner: postgres
--

ALTER TABLE ONLY venlab.analysis ALTER COLUMN a_id SET DEFAULT nextval('venlab.analysis_a_id_seq'::regclass);


--
-- Name: log log_id; Type: DEFAULT; Schema: venlab; Owner: postgres
--

ALTER TABLE ONLY venlab.log ALTER COLUMN log_id SET DEFAULT nextval('venlab.log_log_id_seq'::regclass);


--
-- Data for Name: analysis; Type: TABLE DATA; Schema: backup; Owner: postgres
--

COPY backup.analysis (a_id, s_id, s_stamp, pol, nat, kal, an, dry, date_in, date_out, weight_mea, weight_nrm, weight_cur, weight_dif, density, a_flags, lane, comment, date_exported) FROM stdin;
\.
COPY backup.analysis (a_id, s_id, s_stamp, pol, nat, kal, an, dry, date_in, date_out, weight_mea, weight_nrm, weight_cur, weight_dif, density, a_flags, lane, comment, date_exported) FROM '/backups/2206.dat';

--
-- Data for Name: sample; Type: TABLE DATA; Schema: backup; Owner: postgres
--

COPY backup.sample (s_id, s_stamp, name, weight_net, weight_bru, weight_tar, quantity, distance, date_crumbled, s_flags, lane, comment, date_exported) FROM stdin;
\.
COPY backup.sample (s_id, s_stamp, name, weight_net, weight_bru, weight_tar, quantity, distance, date_crumbled, s_flags, lane, comment, date_exported) FROM '/backups/2207.dat';

--
-- Data for Name: analysis; Type: TABLE DATA; Schema: venlab; Owner: postgres
--

COPY venlab.analysis (a_id, s_id, s_stamp, pol, nat, kal, an, glu, dry, date_in, date_out, weight_mea, weight_nrm, weight_cur, weight_dif, density, a_flags, lane, comment, date_exported) FROM stdin;
\.
COPY venlab.analysis (a_id, s_id, s_stamp, pol, nat, kal, an, glu, dry, date_in, date_out, weight_mea, weight_nrm, weight_cur, weight_dif, density, a_flags, lane, comment, date_exported) FROM '/backups/2208.dat';

--
-- Data for Name: box; Type: TABLE DATA; Schema: venlab; Owner: postgres
--

COPY venlab.box (b_id, name, num_max, type, comment, date_exported) FROM stdin;
\.
COPY venlab.box (b_id, name, num_max, type, comment, date_exported) FROM '/backups/2210.dat';

--
-- Data for Name: boxpos; Type: TABLE DATA; Schema: venlab; Owner: postgres
--

COPY venlab.boxpos (b_id, bpos_id, s_id, s_stamp, date_exported) FROM stdin;
\.
COPY venlab.boxpos (b_id, bpos_id, s_id, s_stamp, date_exported) FROM '/backups/2211.dat';

--
-- Data for Name: log; Type: TABLE DATA; Schema: venlab; Owner: postgres
--

COPY venlab.log (log_id, date_created, level, info, s_id, s_stamp, a_id, date_exported) FROM stdin;
\.
COPY venlab.log (log_id, date_created, level, info, s_id, s_stamp, a_id, date_exported) FROM '/backups/2212.dat';

--
-- Data for Name: sample; Type: TABLE DATA; Schema: venlab; Owner: postgres
--

COPY venlab.sample (s_id, s_stamp, name, weight_net, weight_bru, weight_tar, quantity, distance, date_crumbled, s_flags, lane, comment, date_exported) FROM stdin;
\.
COPY venlab.sample (s_id, s_stamp, name, weight_net, weight_bru, weight_tar, quantity, distance, date_crumbled, s_flags, lane, comment, date_exported) FROM '/backups/2214.dat';

--
-- Data for Name: threshold; Type: TABLE DATA; Schema: venlab; Owner: postgres
--

COPY venlab.threshold (th_id, value_min, value_max, date_changed) FROM stdin;
\.
COPY venlab.threshold (th_id, value_min, value_max, date_changed) FROM '/backups/2215.dat';

--
-- Name: analysis_a_id_seq; Type: SEQUENCE SET; Schema: venlab; Owner: postgres
--

SELECT pg_catalog.setval('venlab.analysis_a_id_seq', 576256, true);


--
-- Name: log_log_id_seq; Type: SEQUENCE SET; Schema: venlab; Owner: postgres
--

SELECT pg_catalog.setval('venlab.log_log_id_seq', 11987488, true);


--
-- Name: analysis analysis_pkey; Type: CONSTRAINT; Schema: venlab; Owner: postgres
--

ALTER TABLE ONLY venlab.analysis
    ADD CONSTRAINT analysis_pkey PRIMARY KEY (a_id);


--
-- Name: box box_pkey; Type: CONSTRAINT; Schema: venlab; Owner: postgres
--

ALTER TABLE ONLY venlab.box
    ADD CONSTRAINT box_pkey PRIMARY KEY (b_id);


--
-- Name: boxpos boxpos_pkey; Type: CONSTRAINT; Schema: venlab; Owner: postgres
--

ALTER TABLE ONLY venlab.boxpos
    ADD CONSTRAINT boxpos_pkey PRIMARY KEY (bpos_id, b_id);


--
-- Name: log log_pkey; Type: CONSTRAINT; Schema: venlab; Owner: postgres
--

ALTER TABLE ONLY venlab.log
    ADD CONSTRAINT log_pkey PRIMARY KEY (log_id);


--
-- Name: sample sample_pkey; Type: CONSTRAINT; Schema: venlab; Owner: postgres
--

ALTER TABLE ONLY venlab.sample
    ADD CONSTRAINT sample_pkey PRIMARY KEY (s_id, s_stamp);


--
-- Name: threshold threshold_pkey; Type: CONSTRAINT; Schema: venlab; Owner: postgres
--

ALTER TABLE ONLY venlab.threshold
    ADD CONSTRAINT threshold_pkey PRIMARY KEY (th_id);


--
-- Name: idx_analysis_a_flags; Type: INDEX; Schema: venlab; Owner: postgres
--

CREATE INDEX idx_analysis_a_flags ON venlab.analysis USING btree (a_flags);


--
-- Name: idx_analysis_date_in; Type: INDEX; Schema: venlab; Owner: postgres
--

CREATE INDEX idx_analysis_date_in ON venlab.analysis USING btree (date_in);


--
-- Name: idx_sample_date_crumbled; Type: INDEX; Schema: venlab; Owner: postgres
--

CREATE INDEX idx_sample_date_crumbled ON venlab.sample USING btree (date_crumbled);


--
-- Name: idx_sample_name; Type: INDEX; Schema: venlab; Owner: postgres
--

CREATE INDEX idx_sample_name ON venlab.sample USING btree (name);


--
-- Name: idx_sample_s_flags; Type: INDEX; Schema: venlab; Owner: postgres
--

CREATE INDEX idx_sample_s_flags ON venlab.sample USING btree (s_flags);


--
-- Name: analysis analysis_s_id_fkey; Type: FK CONSTRAINT; Schema: venlab; Owner: postgres
--

ALTER TABLE ONLY venlab.analysis
    ADD CONSTRAINT analysis_s_id_fkey FOREIGN KEY (s_id, s_stamp) REFERENCES venlab.sample(s_id, s_stamp);


--
-- Name: boxpos boxpos_b_id_fkey; Type: FK CONSTRAINT; Schema: venlab; Owner: postgres
--

ALTER TABLE ONLY venlab.boxpos
    ADD CONSTRAINT boxpos_b_id_fkey FOREIGN KEY (b_id) REFERENCES venlab.box(b_id);


--
-- Name: boxpos boxpos_s_id_fkey; Type: FK CONSTRAINT; Schema: venlab; Owner: postgres
--

ALTER TABLE ONLY venlab.boxpos
    ADD CONSTRAINT boxpos_s_id_fkey FOREIGN KEY (s_id, s_stamp) REFERENCES venlab.sample(s_id, s_stamp);


--
-- Name: log log_a_id_fkey; Type: FK CONSTRAINT; Schema: venlab; Owner: postgres
--

ALTER TABLE ONLY venlab.log
    ADD CONSTRAINT log_a_id_fkey FOREIGN KEY (a_id) REFERENCES venlab.analysis(a_id);


--
-- Name: log log_s_id_fkey; Type: FK CONSTRAINT; Schema: venlab; Owner: postgres
--

ALTER TABLE ONLY venlab.log
    ADD CONSTRAINT log_s_id_fkey FOREIGN KEY (s_id, s_stamp) REFERENCES venlab.sample(s_id, s_stamp);


--
-- Grant permissions to application users
-- This ensures that the application user has CRUD access to all tables, sequences, and functions
-- Note: The POSTGRES_USER is typically 'postgres' but may vary in different deployments (e.g., AWS RDS)
--

-- Create a role for the application if it doesn't exist
-- This allows granting permissions to a specific role instead of PUBLIC
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'venlab_app') THEN
        CREATE ROLE venlab_app;
    END IF;
END
$$;

-- Grant the application role to the postgres user if it exists
-- This ensures the owner can act as the application user
DO $$
BEGIN
    IF EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'postgres') THEN
        GRANT venlab_app TO postgres;
    END IF;
END
$$;

-- Grant usage on schemas
GRANT USAGE ON SCHEMA venlab TO venlab_app;
GRANT USAGE ON SCHEMA backup TO venlab_app;

-- Grant CRUD privileges (not ALL) on all existing tables in venlab schema
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA venlab TO venlab_app;

-- Grant usage on all existing sequences in venlab schema (for auto-increment columns)
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA venlab TO venlab_app;

-- Grant execute on all existing functions in venlab schema
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA venlab TO venlab_app;

-- Grant read access to backup schema tables (no write access needed for backups)
GRANT SELECT ON ALL TABLES IN SCHEMA backup TO venlab_app;

-- Set default privileges for future objects created in venlab schema by postgres role
-- This ensures that objects created by Hibernate (via postgres user) also get proper permissions
DO $$
BEGIN
    IF EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'postgres') THEN
        ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA venlab GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO venlab_app;
        ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA venlab GRANT USAGE, SELECT ON SEQUENCES TO venlab_app;
        ALTER DEFAULT PRIVILEGES FOR ROLE postgres IN SCHEMA venlab GRANT EXECUTE ON FUNCTIONS TO venlab_app;
    END IF;
END
$$;

-- For compatibility: also grant these permissions directly to the postgres user if it exists
-- This provides explicit permissions in addition to role inheritance, ensuring compatibility
-- with applications that may not properly use role membership for permission checks
DO $$
BEGIN
    IF EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'postgres') THEN
        GRANT USAGE ON SCHEMA venlab TO postgres;
        GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA venlab TO postgres;
        GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA venlab TO postgres;
        GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA venlab TO postgres;
    END IF;
END
$$;

--
-- PostgreSQL database dump complete
--

