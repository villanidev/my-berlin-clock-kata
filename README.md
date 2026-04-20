## Known Limitations

The Berlin Clock seconds lamp only encodes **parity** (even/odd).
When converting Berlin → Digital time, the exact second value cannot
be fully recovered. This implementation returns the minimum value
satisfying the parity constraint: `Y → :00`, `O → :01`.

This is an inherent characteristic of the Berlin Clock display, not
a bug in the implementation.
