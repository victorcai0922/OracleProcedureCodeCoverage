declare 
  TYPE TCURSOR IS REF CURSOR;
  v_cur TCURSOR;
  o_errorcode Integer;
  o_errmsg VARCHAR2(400);
begin
  dbms_profiler.start_profiler('sp_get_sys_dict:'||TO_CHAR(SYSDATE,'YYYY-MM-DD HH:MI:SS'));
    tms.pk_right_common.sp_get_sys_dict('ADMIN','88.888.88.88',301100105,v_cur,o_errorcode,o_errmsg);
  dbms_profiler.stop_profiler;
end;