type SetLoadingFn = (val: boolean) => void;

let externalSetLoading: SetLoadingFn = () => {};

export const loadingController = {
  set: (val: boolean) => {
    externalSetLoading(val);
  },
  bind: (fn: SetLoadingFn) => {
    externalSetLoading = fn;
  },
};
