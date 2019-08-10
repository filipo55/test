import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IStudy, defaultValue } from 'app/shared/model/study.model';

export const ACTION_TYPES = {
  FETCH_STUDY_LIST: 'study/FETCH_STUDY_LIST',
  FETCH_STUDY: 'study/FETCH_STUDY',
  CREATE_STUDY: 'study/CREATE_STUDY',
  UPDATE_STUDY: 'study/UPDATE_STUDY',
  DELETE_STUDY: 'study/DELETE_STUDY',
  RESET: 'study/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IStudy>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type StudyState = Readonly<typeof initialState>;

// Reducer

export default (state: StudyState = initialState, action): StudyState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_STUDY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_STUDY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_STUDY):
    case REQUEST(ACTION_TYPES.UPDATE_STUDY):
    case REQUEST(ACTION_TYPES.DELETE_STUDY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_STUDY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_STUDY):
    case FAILURE(ACTION_TYPES.CREATE_STUDY):
    case FAILURE(ACTION_TYPES.UPDATE_STUDY):
    case FAILURE(ACTION_TYPES.DELETE_STUDY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_STUDY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_STUDY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_STUDY):
    case SUCCESS(ACTION_TYPES.UPDATE_STUDY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_STUDY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/studies';

// Actions

export const getEntities: ICrudGetAllAction<IStudy> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_STUDY_LIST,
    payload: axios.get<IStudy>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IStudy> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_STUDY,
    payload: axios.get<IStudy>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IStudy> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_STUDY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IStudy> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_STUDY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IStudy> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_STUDY,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
