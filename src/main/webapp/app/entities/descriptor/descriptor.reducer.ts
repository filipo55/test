import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IDescriptor, defaultValue } from 'app/shared/model/descriptor.model';

export const ACTION_TYPES = {
  FETCH_DESCRIPTOR_LIST: 'descriptor/FETCH_DESCRIPTOR_LIST',
  FETCH_DESCRIPTOR: 'descriptor/FETCH_DESCRIPTOR',
  CREATE_DESCRIPTOR: 'descriptor/CREATE_DESCRIPTOR',
  UPDATE_DESCRIPTOR: 'descriptor/UPDATE_DESCRIPTOR',
  DELETE_DESCRIPTOR: 'descriptor/DELETE_DESCRIPTOR',
  RESET: 'descriptor/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDescriptor>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type DescriptorState = Readonly<typeof initialState>;

// Reducer

export default (state: DescriptorState = initialState, action): DescriptorState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DESCRIPTOR_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DESCRIPTOR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DESCRIPTOR):
    case REQUEST(ACTION_TYPES.UPDATE_DESCRIPTOR):
    case REQUEST(ACTION_TYPES.DELETE_DESCRIPTOR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DESCRIPTOR_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DESCRIPTOR):
    case FAILURE(ACTION_TYPES.CREATE_DESCRIPTOR):
    case FAILURE(ACTION_TYPES.UPDATE_DESCRIPTOR):
    case FAILURE(ACTION_TYPES.DELETE_DESCRIPTOR):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DESCRIPTOR_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_DESCRIPTOR):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DESCRIPTOR):
    case SUCCESS(ACTION_TYPES.UPDATE_DESCRIPTOR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DESCRIPTOR):
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

const apiUrl = 'api/descriptors';

// Actions

export const getEntities: ICrudGetAllAction<IDescriptor> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_DESCRIPTOR_LIST,
    payload: axios.get<IDescriptor>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IDescriptor> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DESCRIPTOR,
    payload: axios.get<IDescriptor>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDescriptor> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DESCRIPTOR,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IDescriptor> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DESCRIPTOR,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDescriptor> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DESCRIPTOR,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
